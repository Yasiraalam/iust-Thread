package com.yasir.iustthread.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.yasir.iustthread.R
import com.yasir.iustthread.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (image, name) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.threads_logo),
            contentDescription = "logo",
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(120.dp)
        )
        Text(
            text = "Developed by Yasir Alam",
            fontWeight = FontWeight(200),
            fontSize = 16.sp,
            color = Color.Magenta,
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(image.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.padding(16.dp)
            )
    }

    LaunchedEffect(true) {
        delay(2000)
        if (FirebaseAuth.getInstance().currentUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
        else {
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
}