package app.aloha.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.viewmodel.SettingViewModel

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    val settingVM = hiltViewModel<SettingViewModel>()
    val theme by settingVM.getTheme().collectAsState(false)

    val negativeMatrix = floatArrayOf(
        -1.0f, 0f, 0f, 0f, 255f,
        0f, -1.0f, 0f, 0f, 255f,
        0f, 0f, -1.0f, 0f, 255f,
        0f, 0f, 0f, 1.0f, 0f
    )

    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.img_logo_transparent),
        colorFilter = ColorFilter.colorMatrix(
            if (theme == true) ColorMatrix(negativeMatrix) else ColorMatrix()
        ),
        contentDescription = "Our app logo"
    )
}