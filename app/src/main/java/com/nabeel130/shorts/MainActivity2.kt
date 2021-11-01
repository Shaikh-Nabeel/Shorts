package com.nabeel130.shorts

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabeel130.shorts.Entity.PrivateNote
import com.nabeel130.shorts.databinding.ActivityMain2Binding
import com.nabeel130.shorts.internalStorageAccess.FileUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.charset.Charset

class MainActivity2 : AppCompatActivity(), IPrivateNotesAdapter {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {

            if(loadFileFromInternalStorage().isEmpty()){
                Log.d("firstTest","no file in internal storage")
                binding.txtViewForPassMsg.text = getString(R.string.setUpPassword)
                binding.submitBtnOfPassword.setOnClickListener {
                    val passwordFirst = binding.passwordFirst.text.toString()
                    val passwordSecond = binding.passwordSecond.text.toString()

                    if (passwordFirst.isEmpty() or passwordFirst.isBlank() or passwordSecond.isEmpty() or passwordSecond.isBlank()) {
                        Toast.makeText(
                            applicationContext,
                            "Fields cannot be Empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (passwordFirst.contentEquals(passwordSecond)) {
                            if (saveSecurityKeyToInternalStorage(passwordFirst)) {
                                binding.relativeLayoutForLock.removeAllViews()
                                showPrivateNotes()
                                Toast.makeText(applicationContext,"Password is set",Toast.LENGTH_SHORT).show()
                            }
                            else
                                Toast.makeText(applicationContext,"Something went wrong. Try again",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "!! Both password must be equal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }else{
                val files = loadFileFromInternalStorage()
                binding.txtViewForPassMsg.text = getString(R.string.enterPassword)
                binding.passwordSecond.visibility = View.GONE
                binding.submitBtnOfPassword.setOnClickListener {
                    val password = binding.passwordFirst.text.toString()
                    val actualPassword = files[0]
//                    Log.d("firstTest",actualPassword)
                    if(password.contentEquals(actualPassword)){
                        binding.relativeLayoutForLock.removeAllViews()
                        showPrivateNotes()
                    }
                }
            }
        }

    }

    private fun showPrivateNotes(){
        binding.customToolBarOfMainAct2.title = getString(R.string.app_name)
        binding.customToolBarOfMainAct2.setTitleTextColor(Color.WHITE)

        val adapter = PrivateNoteRVAdapter(this)
        binding.recyclerViewOfPrivateNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOfPrivateNotes.adapter = adapter

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        viewModel.allPrivateNote.observe(this,{
            it?.let {
                adapter.updateNotes(it as ArrayList<PrivateNote>)
            }
        })
    }

    private suspend fun loadFileFromInternalStorage(): List<String> {
        val allFiles = filesDir.listFiles()
        return FileUtility.loadFileFromInternalStorage(allFiles)
    }

    private fun saveSecurityKeyToInternalStorage(securityKey: String): Boolean {
        return try{
            openFileOutput("security_key.txt", MODE_PRIVATE).use {
                it.write(securityKey.toByteArray(Charset.forName("UTF-8")))
            }
            true
        }catch (e: IOException){
            e.printStackTrace()
            false
        }
    }

    private fun deleteFile(){
        deleteFile("security_key.txt")
    }

    override fun onDeleteItemClicked(note: PrivateNote) {
        viewModel.deletePrivateNote(note)
    }

    override fun onTextItemClicked(note: PrivateNote) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.edit_notes)
        val editText = dialog.findViewById<EditText>(R.id.editTextOfNote)
        editText.setText(note.pText)
        val updateNotes = dialog.findViewById<Button>(R.id.updateNotesBtn)

        updateNotes.setOnClickListener {
            note.pText= editText.text.toString()
            viewModel.updatePrivateNote(note)
            dialog.dismiss()
            Toast.makeText(this,"submitted", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }
}