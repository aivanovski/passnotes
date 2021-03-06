package com.ivanovsky.passnotes.presentation.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanovsky.passnotes.R
import com.ivanovsky.passnotes.data.ObserverBus
import com.ivanovsky.passnotes.data.entity.Note
import com.ivanovsky.passnotes.domain.LocaleProvider
import com.ivanovsky.passnotes.domain.ResourceProvider
import com.ivanovsky.passnotes.domain.entity.PropertyFilter
import com.ivanovsky.passnotes.domain.interactor.ErrorInteractor
import com.ivanovsky.passnotes.domain.interactor.note.NoteInteractor
import com.ivanovsky.passnotes.presentation.core_mvvm.BaseScreenViewModel
import com.ivanovsky.passnotes.presentation.core_mvvm.DefaultScreenStateHandler
import com.ivanovsky.passnotes.presentation.core_mvvm.ScreenState
import com.ivanovsky.passnotes.presentation.core_mvvm.ViewModelTypes
import com.ivanovsky.passnotes.presentation.core_mvvm.event.SingleLiveEvent
import com.ivanovsky.passnotes.presentation.core_mvvm.viewmodels.NotePropertyCellViewModel
import com.ivanovsky.passnotes.presentation.note.converter.toCellModels
import com.ivanovsky.passnotes.util.StringUtils.EMPTY
import com.ivanovsky.passnotes.util.formatAccordingLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NoteViewModel(
    private val interactor: NoteInteractor,
    private val errorInteractor: ErrorInteractor,
    private val resourceProvider: ResourceProvider,
    private val localeProvider: LocaleProvider,
    private val observerBus: ObserverBus
) : BaseScreenViewModel(), ObserverBus.NoteContentObserver {

    val viewTypes = ViewModelTypes()
        .add(NotePropertyCellViewModel::class, R.layout.cell_note_property)

    val screenStateHandler = DefaultScreenStateHandler()
    val screenState = MutableLiveData<ScreenState>(ScreenState.notInitialized())

    val actionBarTitle = MutableLiveData<String>()
    val modifiedText = MutableLiveData<String>()
    val showEditNoteScreenEvent = SingleLiveEvent<Note>()
    val showSnackbarMessageEvent = SingleLiveEvent<String>()

    private val cellFactory = NoteCellFactory()
    private var note: Note? = null
    private var noteUid: UUID? = null

    init {
        observerBus.register(this)
        subscribeToCellEvents()
    }

    override fun onCleared() {
        super.onCleared()
        observerBus.unregister(this)
    }

    fun start(noteUid: UUID) {
        this.noteUid = noteUid

        loadData()
    }

    fun onFabButtonClicked() {
        val note = this.note ?: return
        showEditNoteScreenEvent.call(note)
    }

    override fun onNoteContentChanged(groupUid: UUID, oldNoteUid: UUID, newNoteUid: UUID) {
        if (oldNoteUid == noteUid) {
            noteUid = newNoteUid

            loadData()
        }
    }

    private fun loadData() {
        val noteUid = this.noteUid ?: return

        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                interactor.getNoteByUid(noteUid)
            }

            if (result.isSucceededOrDeferred) {
                val note = result.obj
                this@NoteViewModel.note = note

                actionBarTitle.value = note.title
                modifiedText.value =
                    note.modified.formatAccordingLocale(localeProvider.getSystemLocale())

                val filter = PropertyFilter.Builder()
                    .visible()
                    .notEmpty()
                    .excludeTitle()
                    .sortedByType()
                    .build()

                val models = filter.apply(note.properties)
                    .toCellModels()

                setCellElements(cellFactory.createCellViewModels(models, eventProvider))

                screenState.value = ScreenState.data()
            } else {
                val message = errorInteractor.processAndGetMessage(result.error)
                screenState.value = ScreenState.error(message)
            }
        }
    }

    private fun subscribeToCellEvents() {
        eventProvider.subscribe(this) { event ->
            if (event.containsKey(NotePropertyCellViewModel.COPY_BUTTON_CLICK_EVENT)) {
                val text = event.getString(NotePropertyCellViewModel.COPY_BUTTON_CLICK_EVENT)
                    ?: EMPTY
                onCopyButtonClicked(text)
            }
        }
    }

    private fun onCopyButtonClicked(text: String) {
        interactor.copyToClipboardWithTimeout(text)

        val delayInSeconds = interactor.getTimeoutValueInMillis() / 1000L

        val message = resourceProvider.getString(
            R.string.copied_clipboard_will_be_cleared_in_seconds,
            delayInSeconds
        )
        showSnackbarMessageEvent.call(message)
    }
}