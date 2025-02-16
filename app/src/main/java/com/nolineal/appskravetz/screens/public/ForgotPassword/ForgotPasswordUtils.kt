package com.nolineal.appskravetz.screens.public.ForgotPassword

fun generateHeadingText(state: ForgotPasswordFormState): String {
    return when (state) {
        ForgotPasswordFormState.INITIAL -> {
            "Ingresa el correo electrónico asociado a tu usuario:"
        }

        ForgotPasswordFormState.ERROR -> {
            "Ha ocurrido un error, inténtalo nuevamente"
        }
        

        ForgotPasswordFormState.NO_EMAIL_GIVEN -> {
            "Debes proporcionar un correo electrónico"
        }

        ForgotPasswordFormState.SUCCESS -> {
            "Contraseña cambiada con éxito! Haz click en el botón para volver a autenticarte con tu nueva contraseña."
        }
    }
}