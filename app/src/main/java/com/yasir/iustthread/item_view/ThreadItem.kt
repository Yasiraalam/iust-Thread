package com.yasir.iustthread.item_view

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.yasir.iustthread.model.ThreadModel
import com.yasir.iustthread.model.UserModel

@Composable
fun ThreadItem(
    thread:ThreadModel,
    users:UserModel,
    navHostController: NavHostController,
    userId:String
) {
    Column {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val (userImage,
                userName,
                date,
                time,
                title,
                image) = createRefs()
            Image(
                painter = rememberAsyncImagePainter(model = users.imageUri),
                contentDescription = "userImage",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop

            )
            Text(
                text = users.username,
                style = TextStyle(
                    fontSize = 20.sp
                ),
                modifier = Modifier.constrainAs(userName) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 12.dp)
                    bottom.linkTo(userImage.bottom)
                }
            )
            Text(
                text = thread.thread,
                style = TextStyle(
                    fontSize = 14.sp
                ),
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(userName.bottom, margin = 5.dp)
                    start.linkTo(userImage.start)
                }
            )
            if(thread.image !=""){
                Card(
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(title.bottom, margin = 5.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
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
        }
        Divider(color= Color.LightGray, thickness = 1.dp)
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
    val infiniteTransition = rememberInfiniteTransition()
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


