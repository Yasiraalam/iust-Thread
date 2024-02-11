package com.yasir.iustthread.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yasir.iustthread.screens.AddThreads
import com.yasir.iustthread.screens.BottomNav
import com.yasir.iustthread.screens.Home
import com.yasir.iustthread.screens.Login
import com.yasir.iustthread.screens.Notification
import com.yasir.iustthread.screens.OtherUsers
import com.yasir.iustthread.screens.Profile
import com.yasir.iustthread.screens.Register
import com.yasir.iustthread.screens.Search
import com.yasir.iustthread.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.routes
    ){
        composable(Routes.Splash.routes){
            Splash(navController)
        }

        composable(Routes.Home.routes){
            Home(navController)

        }

        composable(Routes.Notification.routes){
            Notification()
        }

        composable(Routes.Search.routes){
            Search(navController)
        }

        composable(Routes.AddThread.routes){
            AddThreads(navController)
        }

        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.Login.routes){
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUsers.routes){
            val data = it.arguments!!.getString("data")
            OtherUsers(navController,data!!)
        }
    }
}