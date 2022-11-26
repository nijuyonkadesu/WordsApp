package com.example.wordsapp

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.data.SettingsDataStore
import com.example.wordsapp.databinding.FragmentLetterListBinding
import kotlinx.coroutines.launch

// check commit 9d7f928ede0fa7415e442428f33aaa18c4d268b4 for implementation using activities
private lateinit var SettingsDataStore: SettingsDataStore

class LetterListFragment : Fragment() {
    private var _binding: FragmentLetterListBinding? = null // cannot use Fragment unless a View is ready. For time being, it has to be null < onCreate() to onCreateView() >, only then can bind views, properties
    private val binding get() = _binding!! // _name not meant to access directly
    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true

    override fun onCreate(savedInstanceState: Bundle?) { // 1
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView( // 2
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // 3
        recyclerView = binding.recyclerView // RecyclerView automatically was attached, no need to call findViewById()
        SettingsDataStore = SettingsDataStore(requireContext())
        SettingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            isLinearLayoutManager = value
            // set layout
            chooseLayout()
            // Redraw the menu
            activity?.invalidateOptionsMenu()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // since view no longer exists
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    fun chooseLayout(){ // sets respective layout manager using boolean property, also attaches linearAdapter() to the recyclerView
        if(isLinearLayoutManager){
            recyclerView.layoutManager = LinearLayoutManager(context) // replace this with context, Fragments do not have Context property as activity
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        }
        recyclerView.adapter = LetterAdapter()
    }

    fun setIcon(menuItem: MenuItem?){ // sets correct icon using boolean property
        if(menuItem == null) return
        menuItem.icon =
            if(isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout) // this.requireContext()
            else
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){ // handle only layout icon when tapped -> item.itemId is any item that is being tapped
            R.id.action_switch_layout -> {
                isLinearLayoutManager =! isLinearLayoutManager // toggle layout property when menuItem is pressed
                lifecycleScope.launch {
                    SettingsDataStore.saveLayoutToPreferencesStore(isLinearLayoutManager, requireContext())
                }
                setIcon(item)
                true
            }
            else ->
                super.onOptionsItemSelected(item) // for other items in menu, use the default event handler otherwise
        }
    }
}
