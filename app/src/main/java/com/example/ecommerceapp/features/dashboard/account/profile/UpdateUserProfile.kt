package com.example.ecommerceapp.features.dashboard.account.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerceapp.core.ui.AppButton
import com.example.ecommerceapp.core.ui.AppLoadingDialog
import com.example.ecommerceapp.core.ui.AppTextField
import com.example.ecommerceapp.core.ui.EcommerceAppBar
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.core.utils.isValidEmail
import com.example.ecommerceapp.core.utils.isValidPhoneNumber
import com.example.ecommerceapp.features.user.data.UpdateUserRequest
import com.example.ecommerceapp.features.user.presentation.UserViewModel

@Composable
fun UpdateUserProfile(
    title: String = "",
    event: ProfileUpdateEvent,
    onBackClick: () -> Unit
) {

    var userDataState by remember {mutableStateOf("")}
    var userViewModel: UserViewModel = hiltViewModel()
    val userUpdatedDataState by userViewModel.updateUserState.collectAsState()
    var isErrorData by remember { mutableStateOf(false) }
    val currentUser by userViewModel.userDetails.collectAsState()
    var isLoading by remember {mutableStateOf(false)}
    var isError by remember { mutableStateOf(false) }
    var isSucces by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf("") }
    LaunchedEffect(userUpdatedDataState){

        when(userUpdatedDataState){
            is Resource.Error -> {
                isLoading = false
                isError = true

            }
            is Resource.Idle -> {}
            is Resource.Loading -> {
                isLoading = true
            }
            is Resource.Success -> {
                isLoading = false
                isSucces = true


            }
        }
    }
    LaunchedEffect(Unit) {

        userViewModel.loadUserDetails()
    }
    Scaffold(
        topBar = {
            EcommerceAppBar(title = title, onBackClick = {

                onBackClick()
            })
        }
    ) { innerPadding ->
        Column(
            modifier = androidx.compose.ui.Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when(event){
                is ProfileUpdateEvent.OnUpdateEmail -> {

                    UpdateProfileItem(
                        userDataState = userDataState,
                        title = "Email",
                        onValueChange = { userDataState = it },
                        onUpdateUserData = {
                            Log.d("TApdlpfdfG", "UpdateProfileItem: ${currentUser.toString()}")
                            if(userDataState.isNotEmpty()&&userDataState.isValidEmail()){
                                if(currentUser!=null){
                                    val user = currentUser!!.id.let {
                                        UpdateUserRequest(
                                            user_id = it,
                                            email = currentUser!!.email,
                                            name = currentUser!!.name,
                                            phone = currentUser!!.phone,
                                        )
                                    }
                                    userViewModel.updateUser(user.copy(email = userDataState))
                                }else{
                                    isErrorData = true
                                    errorMessage = "User Not Found"
                                }

                            }else{
                                isErrorData = true
                            }

                        }
                    )
                }
                is ProfileUpdateEvent.OnUpdatePhone -> {
                    UpdateProfileItem(
                        userDataState = userDataState,
                        title = "Email",
                        onValueChange = { userDataState = it },
                        onUpdateUserData = {
                            Log.d("TApdlpfdfG", "UpdateProfileItem: ${currentUser.toString()}")
                            if(userDataState.isNotEmpty()&&userDataState.isValidPhoneNumber()){
                                if(currentUser!=null){
                                    val user = currentUser!!.id.let {
                                        UpdateUserRequest(
                                            user_id = it,
                                            email = currentUser!!.email,
                                            name = currentUser!!.name,
                                            phone = userDataState,
                                        )
                                    }
                                    userViewModel.updateUser(user.copy(phone = userDataState))
                                }else{
                                    isErrorData = true
                                    errorMessage = "User Not Found"
                                }

                            }else{
                                isErrorData = true
                            }

                        }
                    )
                }
                is ProfileUpdateEvent.onUpdatePassword -> {

                }
            }


            if(isErrorData){
                Toast.makeText(LocalContext.current,"Enter Valid Data", Toast.LENGTH_SHORT).show()
            }
            if(isError){
                if(errorMessage.isNotEmpty()){
                    Toast.makeText(LocalContext.current,errorMessage, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(LocalContext.current,"Faild To Update Please Try Again", Toast.LENGTH_SHORT).show()
                }

                isError = false
            }
            if(isSucces){
                Toast.makeText(LocalContext.current,"Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                isSucces = false
            }
            if(isLoading){
               AppLoadingDialog {
                   isLoading = false
               }
            }


        }
    }
}
@Composable
fun UpdateProfileItem(userDataState:String,title:String,onValueChange:(String)->Unit,
                      onUpdateUserData:()->Unit
                      ){
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AppTextField(
            value = userDataState,
            onValueChange = onValueChange,
            label = "Enter Your ${title} ",
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Update") {
            onUpdateUserData()
        }
    }

}
sealed class ProfileUpdateEvent{
    data class OnUpdatePhone(val name:String): ProfileUpdateEvent()
    data class OnUpdateEmail(val email:String): ProfileUpdateEvent()
    data class onUpdatePassword(val phone:String): ProfileUpdateEvent()

}