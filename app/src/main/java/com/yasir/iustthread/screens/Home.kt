package com.yasir.iustthread.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.yasir.iustthread.R
import com.yasir.iustthread.item_view.ThreadItem
import com.yasir.iustthread.viewmodel.HomeViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun Home(navHostController:NavHostController) {
    val context = LocalContext.current
    val homeViewModel:HomeViewModel = viewModel()
    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)

    Column{
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "IUST-Thread",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight(12),
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 30.dp))
            Image(
                painter = painterResource(id = R.drawable.threads_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 5.dp)
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        LazyColumn {
            items(threadAndUsers?: emptyList()){pairs->
                val threadId = pairs.first.thread
                threadAndUsers?.let {
                    ThreadItem(
                        threadId = threadId,
                        thread = pairs.first,
                        users =pairs.second,
                        navHostController = navHostController,
                        userId = FirebaseAuth.getInstance().currentUser!!.uid,
                        homeViewModel = homeViewModel,
                        threadAndUsers = it
                    )
                }
            }
        }
    }

}