package com.ivanovsky.passnotes

private val APP_CONFIG = App.AppConfig(
    isStethoEnabled = false,
    isKoinEnabled = false
)

class TestApp : App(APP_CONFIG)