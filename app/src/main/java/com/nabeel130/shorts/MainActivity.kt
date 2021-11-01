package com.nabeel130.shorts

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import com.nabeel130.shorts.Entity.Note
import com.nabeel130.shorts.Entity.PrivateNote
import com.nabeel130.shorts.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), INotesAdapter {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private var isPrivateModeEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textOfNotes.inputType = InputType.TYPE_NULL
        binding.customToolBarOfMainAct.title = getString(R.string.app_name)
        binding.customToolBarOfMainAct.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.customToolBarOfMainAct)

        binding.recyclerViewOfNotes.layoutManager = LinearLayoutManager(this)
        val adapter = NoteRVAdapter(this)
        binding.recyclerViewOfNotes.adapter = adapter

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, {
            it?.let {
                adapter.updateNotes(it as ArrayList<Note>)
            }
        })

        binding.submitBtn.setOnClickListener{
            val noteText = binding.textOfNotes.text.toString()
            if(noteText.isNotEmpty() && noteText.isNotBlank()){

                if(isPrivateModeEnabled){
//                    val allFiles = filesDir.listFiles()
//                    if(FileUtility.loadFileFromInternalStorage().isEmpty()) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Cannot Save, Enable password first",
//                            Toast.LENGTH_SHORT
//                        );
//                    }
                    Log.d("firstTest","inserting private note")
                    viewModel.insertPrivateNote(PrivateNote(noteText.trim()))
                }else{
                    viewModel.insertNote(Note(noteText.trim()))
                }
                binding.textOfNotes.setText("")
                Toast.makeText(this,"submitted",Toast.LENGTH_SHORT).show()
            }
        }
        binding.textOfNotes.setOnClickListener {
            showSoftKeyboard()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.privateNoteBtnOfMenu -> {
                startActivity(Intent(this,MainActivity2::class.java))
            }
            R.id.privateModeEnabled -> {
                isPrivateModeEnabled = if(isPrivateModeEnabled){
                    item.setIcon(R.drawable.ic_baseline_lock_open_24)
                    Toast.makeText(this,"Private Mode Disabled", Toast.LENGTH_SHORT).show()
                    false
                }else{
                    item.setIcon(R.drawable.ic_baseline_lock_24)
                    Toast.makeText(this,"Private Mode Enabled", Toast.LENGTH_SHORT).show()
                    true
                }
            }
            R.id.deleteAllBtnOfMenu ->Toast.makeText(this,"private",Toast.LENGTH_SHORT).show()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSoftKeyboard() {
        binding.textOfNotes.isSingleLine = false
        binding.textOfNotes.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        binding.textOfNotes.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.textOfNotes.setLines(3)
        binding.textOfNotes.isVerticalScrollBarEnabled = true
        binding.textOfNotes.movementMethod = ScrollingMovementMethod.getInstance()
        binding.textOfNotes.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
        binding.textOfNotes.maxLines = 5
        binding.textOfNotes.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.textOfNotes,InputMethodManager.SHOW_FORCED)
    }

    override fun onTextItemClicked(note: Note) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.edit_notes)
        val editText = dialog.findViewById<EditText>(R.id.editTextOfNote)
        editText.setText(note.text)
        val updateNotes = dialog.findViewById<Button>(R.id.updateNotesBtn)

        updateNotes.setOnClickListener {
            note.text = editText.text.toString()
            viewModel.updateNote(note)
            dialog.dismiss()
            Toast.makeText(this,"submitted",Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    override fun onDeleteItemClicked(note: Note) {
        viewModel.deleteNote(note)
    }
}