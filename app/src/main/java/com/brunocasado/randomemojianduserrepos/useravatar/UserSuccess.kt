package com.brunocasado.randomemojianduserrepos.useravatar

import com.brunocasado.randomemojianduserrepos.core.exception.Success

sealed class UserSuccess : Success.FeatureSuccess(){
    object SaveUserSuccess : UserSuccess()
}