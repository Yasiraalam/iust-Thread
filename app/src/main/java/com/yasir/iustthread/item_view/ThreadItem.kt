package com.yasir.iustthread.item_view

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.auth.User
import com.yasir.iustthread.R
import com.yasir.iustthread.model.ThreadModel
import com.yasir.iustthread.model.UserModel
import com.yasir.iustthread.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navHostController: NavHostController,
    userId: String,
    threadId: String,
    homeViewModel: HomeViewModel,
    threadAndUsers: List<Pair<ThreadModel, UserModel>>
) {

    var shimmerVisibleState = remember { mutableStateOf(true) }
    LaunchedEffect(shimmerVisibleState) {
        launch {
            delay(1500)
            shimmerVisibleState.value = false
        }
    }
    Column {
        if (shimmerVisibleState.value ) {
            ShimmerItem()
        } else {
            ThreadContent(
                thread = thread,
                users = users,
                threadId = threadId,
                homeViewModel =homeViewModel
            )

        }
    }

}

@Composable
fun ThreadContent(
    thread: ThreadModel,
    users: UserModel,
    threadId: String,
    homeViewModel: HomeViewModel
) {
    var isLiked by remember { mutableStateOf(false) }
    var likes by remember { mutableStateOf(thread.likes) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users.imageUri),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = users.username,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        // Thread Content
        Text(
            text = thread.thread,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .fillMaxWidth(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        if(thread.image !=""){
            Card(
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    painter = rememberAsyncImagePainter(model = thread.image),
                    contentDescription = "userimage",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        LaunchedEffect(threadId) {
            homeViewModel.likeThread(threadId, thread.userId) { likesCount ->
                likes = likesCount
            }
        }
        // Like and Comment Section
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) Color.Red else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        isLiked = !isLiked
                        Toast
                            .makeText(context, "threadID:$threadId", Toast.LENGTH_SHORT)
                            .show()
                        homeViewModel.likeThread(threadId, thread.userId) { likesCount ->
                            likes = likesCount
                        }
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = likes.toString(),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.comment_icon),
                contentDescription = "Comment",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Navigate to comment section or perform other action
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = thread.comments,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ShimmerItem() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shimmerEffect()
        )
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scaleX by infiniteTransition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors= listOf(
                Color(0xFFC9C1C1),
                Color(0xFF646262),
                Color(0xFFC9C1C1)
            ),
            start = Offset(scaleX,0f),
            end = Offset(scaleX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size =it.size
    }
}


