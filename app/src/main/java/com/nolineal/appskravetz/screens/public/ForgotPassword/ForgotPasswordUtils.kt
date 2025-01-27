package com.nolineal.appskravetz.screens.public.ForgotPassword

fun generateHeadingText(state: ForgotPasswordFormState): String {
    return when (state) {
        ForgotPasswordFormState.INITIAL -> {
            "Ingresa el correo electrónico asociado a tu usuario:"
        }

        ForgotPasswordFormState.ERROR -> {
            "Ha ocurrido un error, inténtalo nuevamente"
        }

        ForgotPasswordFormState.USER_NOT_FOUND -> {
            "Lo sentimos, usuario no ha sido encontrado, revisa el e-mail"
        }

        ForgotPasswordFormState.FOUND_USER -> {
            "Ingresa tu nueva contraseña, asegúrate que coincida en ambos campos"
        }

        ForgotPasswordFormState.NO_EMAIL_GIVEN -> {
            "Debes proporcionar un correo electrónico"
        }

        ForgotPasswordFormState.SUCCESS -> {
            "Contraseña cambiada con éxito! Haz click en el botón para volver a autenticarte con tu nueva contraseña."
        }
    }
}