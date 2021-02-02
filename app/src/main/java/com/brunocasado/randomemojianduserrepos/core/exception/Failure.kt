package com.brunocasado.randomemojianduserrepos.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    abstract class FeatureFailure: Failure()
}