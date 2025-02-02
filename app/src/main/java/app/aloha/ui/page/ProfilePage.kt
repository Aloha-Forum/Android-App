package app.aloha.ui.page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.activity.AuthActivity
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.Avatar
import app.aloha.ui.component.BodyText
import app.aloha.ui.component.GitHubButton
import app.aloha.ui.component.Label
import app.aloha.ui.component.PrivacyPolicyButton
import app.aloha.ui.component.ProfileButton
import app.aloha.ui.component.ThemeButton
import app.aloha.ui.component.Title
import app.aloha.ui.component.TopAppBar
import app.aloha.viewmodel.AuthViewModel
import app.aloha.viewmodel.AvatarViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Column {
        ProfileTopBar()
        Column(
            modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Label("General", fontWeight = FontWeight.Bold)

            ThemeButton()

            HorizontalDivider(Modifier.padding(bottom = 12.dp))

            Label("System", fontWeight = FontWeight.Bold)

            GitHubButton()
            PrivacyPolicyButton()

            ProfileButton(
                "App Ver.",
                R.drawable.ic_construct,
                "App Ver.",
                data = LocalContext.current
                    .packageManager
                    .getPackageInfo(LocalContext.current.packageName, 0)
                    .versionName
            )
        }
    }
}

@Composable
private fun ProfileTopBar() {
    val authVM: AuthViewModel = hiltViewModel()
    val isLogin by authVM.isLogin.collectAsState(false)
    val user by authVM.user.collectAsState(null)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val avatarVM = hiltViewModel<AvatarViewModel>()

    var reloadAvatar by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            coroutineScope.launch {
                val filePart = prepareFilePart(it, context)
                if (filePart != null) {
                    avatarVM.uploadAvatar(filePart, {
                        // Toggle reloadAvatar to trigger recomposition
                        reloadAvatar = !reloadAvatar
                        println("Upload successful")
                    }) {
                        println("Upload failed")
                    }
                }
            }
        }
    }

    if (!isLogin || user == null) {
        val activity = LocalContext.current as? Activity
        Box(
            Modifier.clickable { activity?.startActivity(Intent(activity, AuthActivity::class.java)) }
        ) {
            TopAppBar(
                "Guest",
                "Click here to log in..."
            ) {
                Box(Modifier.padding(end = 12.dp)) {
                    Avatar(null, reloadAvatar)
                }
            }
        }
    } else {
        var showLogOutDialog by remember { mutableStateOf(false) }
        if (showLogOutDialog) {
            LogOutDialog(
                onDismissRequest = { showLogOutDialog = false },
                onConfirmation = { authVM.signOut() }
            )
        }
        Box(Modifier.clickable { showLogOutDialog = true }) {
            TopAppBar(title = user!!.id) {
                Box(Modifier.padding(end = 12.dp)) {
                    Avatar(user!!.id, reloadAvatar) { // Pass reloadAvatar
                        launcher.launch("image/*")
                    }
                }
            }
        }
    }
}

@Composable
fun LogOutDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.92f),
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        ),
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = "Cancel")
            }
        },
        title = { Title("Sign out") },
        text = { BodyText("Are you ready to step away for a while?") }
    )
}

fun prepareFilePart(uri: Uri, context: Context): MultipartBody.Part? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = getFileName(uri, context)

        val requestFile = inputStream?.let {
            object : RequestBody() {
                override fun contentType(): MediaType? {
                    return "image/jpeg".toMediaTypeOrNull()
                }

                override fun writeTo(sink: BufferedSink) {
                    inputStream.copyTo(sink.outputStream())
                }
            }
        }

        if (requestFile != null)
            return MultipartBody.Part.createFormData("avatar", fileName, requestFile)
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

private fun getFileName(uri: Uri, context: Context): String {
    var name = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                name = it.getString(index)
            }
        }
    }
    return name
}