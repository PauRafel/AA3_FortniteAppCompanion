package com.example.aa2_paurafeldiazhernandez

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var imgProfile: ImageView
    private lateinit var txtUserName: TextView
    private lateinit var txtUserEmail: TextView
    private lateinit var txtLoginMethod: TextView
    private lateinit var btnSignOut: Button
    private lateinit var btnChangePassword: Button
    private lateinit var editCurrentPassword: EditText
    private lateinit var editNewPassword: EditText
    private lateinit var txtError: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("491085797124-ovgcgkh3tg7lm5dnjt960q6g16t79p5a.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        imgProfile = view.findViewById(R.id.img_profile)
        txtUserName = view.findViewById(R.id.txt_user_name)
        txtUserEmail = view.findViewById(R.id.txt_user_email)
        txtLoginMethod = view.findViewById(R.id.txt_login_method)
        btnSignOut = view.findViewById(R.id.btn_sign_out)
        btnChangePassword = view.findViewById(R.id.btn_change_password)

        loadUserInfo()

        btnSignOut.setOnClickListener {
            performSignOut()
        }

        btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        applyThemeToButtons()
    }

    private fun loadUserInfo() {
        val firebaseUser = auth.currentUser
        val googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (googleAccount != null) {
            displayGoogleUserInfo(googleAccount)
            btnChangePassword.visibility = View.GONE
        } else if (firebaseUser != null) {
            displayFirebaseUserInfo(firebaseUser)
            btnChangePassword.visibility = View.VISIBLE
        }
    }

    private fun displayGoogleUserInfo(account: GoogleSignInAccount) {
        txtUserName.text = account.displayName
        txtUserEmail.text = account.email
        txtLoginMethod.text = "Logged in with: Google"

        account.photoUrl?.let { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .into(imgProfile)
        }
    }

    private fun displayFirebaseUserInfo(user: com.google.firebase.auth.FirebaseUser) {
        txtUserName.text = user.displayName
        txtUserEmail.text = user.email
        txtLoginMethod.text = "Logged in with: Email/Password"
        imgProfile.setImageResource(R.drawable.profile_image)
    }

    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_change_password, null)

        editCurrentPassword = dialogView.findViewById(R.id.edit_current_password)
        editNewPassword = dialogView.findViewById(R.id.edit_new_password)
        txtError = dialogView.findViewById(R.id.txt_error)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Cambiar", null)
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                clearError()
                val currentPassword = editCurrentPassword.text.toString()
                val newPassword = editNewPassword.text.toString()

                if (validatePasswordInput(currentPassword, newPassword)) {
                    changePassword(currentPassword, newPassword, dialog)
                }
            }
        }

        dialog.show()
    }

    private fun validatePasswordInput(currentPassword: String, newPassword: String): Boolean {
        if (currentPassword.isEmpty()) {
            showError("Introduce your current password")
            return false
        }

        if (newPassword.isEmpty()) {
            showError("Introduce the new password")
            return false
        }

        if (newPassword.length < 6) {
            showError("The password must have more than 6 characters")
            return false
        }

        return true
    }

    private fun showError(msg: String) {
        txtError.text = msg
        txtError.visibility = View.VISIBLE
    }

    private fun clearError() {
        txtError.visibility = View.GONE
    }

    private fun changePassword(currentPassword: String, newPassword: String, dialog: AlertDialog) {
        val user = auth.currentUser
        val email = user?.email

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            dialog.dismiss()
                        }
                }
                .addOnFailureListener {
                    showError("Your current password is incorrect")
                }
        }
    }

    private fun performSignOut() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun applyThemeToButtons() {
        val primaryColor = ThemeManager.getPrimaryColor(requireContext())
        btnSignOut.setBackgroundColor(primaryColor)
    }
}