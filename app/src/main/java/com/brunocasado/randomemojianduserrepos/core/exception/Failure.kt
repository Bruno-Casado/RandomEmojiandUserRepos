package com.brunocasado.randomemojianduserrepos.core.exception

sealed class Failure {
    abstract class FeatureFailure: Failure()
}