package com.brunocasado.randomemojianduserrepos.core.exception

sealed class Success {
    abstract class FeatureSuccess : Success()
}