package com.example.nitters.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nitters.databinding.FragmentNoteBinding
import com.example.nitters.di.utils.NetworkResult
import com.example.nitters.fireViewModel.FireNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val fireNotes by viewModels<FireNoteViewModel>()

    //private var note: NoteResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNotes()
        bindHandlers()
        //bindObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setNotes() {



//        val jsonNote = arguments?.getString("note")
//        if(jsonNote != null) {
//            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
//            note?.let {
//                binding.txtTitle.setText(it.title)
//                binding.txtDescription.setText(it.description)
//            }
//
//        } else {
//            binding.addEditText.text = "Add Note"
//        }
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {

//            note?.let{                            //For delete Notes using repository
//                noteViewModel.deleteNotes(it._id)
//            }
        }
        binding.btnSubmit.setOnClickListener {

            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()

            if(title.isNotEmpty() || description.isNotEmpty() || fireNotes.status()){
                fireNotes.addNote(title, description)
                    Toast.makeText(context,"Note Saved", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()

            } else {
                Toast.makeText(context,"Something is not right", Toast.LENGTH_SHORT).show()
            }

//            val title = binding.txtTitle.text.toString()
//            val description = binding.txtDescription.text.toString()
//            val noteRequest = NoteRequest(description, title)
//            if(note == null) { //New Note Because its null
//                noteViewModel.createNotes(noteRequest)
//            } else { //Update Note because its not null
//                noteViewModel.updateNotes(noteRequest, note!!._id)
//            }
        }
    }

//    private fun bindObservers() {
//        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
//            when (it) {
//                is NetworkResult.Success -> {
//                    findNavController().popBackStack()
//                }
//
//                is NetworkResult.Error -> {
//
//                }
//
//                is NetworkResult.Loading -> {
//
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}