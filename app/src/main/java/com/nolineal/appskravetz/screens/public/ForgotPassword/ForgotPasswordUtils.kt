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
            "Hemos enviado exitosamente las instrucciones para el reestablecimiento de contraseña al correo que has ingresado. Por favor, revisa tu bandeja de entrada y luego realiza login con tus nuevas credenciales!"
        }
    }
}