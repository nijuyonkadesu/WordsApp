/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.wordsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.ActivityMainBinding

/**
 * Main Activity and entry point for the app. Displays a RecyclerView of letters.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        chooseLayout() // set LinearLayout by default
    }

    fun chooseLayout(){ // sets respective layout manager using boolean property, also attaches linearAdapter() to the recyclerView
        if(isLinearLayoutManager){
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        recyclerView.adapter = LetterAdapter()
    }

    fun setIcon(menuItem: MenuItem?){ // sets correct icon using boolean property
        if(menuItem == null) return
        menuItem.icon =
            if(isLinearLayoutManager)
                ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
            else
                ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // makes sure if correct menu icon is set when inflating layout on launching app
        menuInflater.inflate(R.menu.layout_menu, menu) // inflate menu layout manually
        val layoutButton = menu?.findItem(R.id.action_switch_layout)// layout icon ID
        setIcon(layoutButton)
        return true // options menu wanted to be created, hence return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){ // handle only layout icon when tapped -> item.itemId is any item that is being tapped
            R.id.action_switch_layout -> {
                isLinearLayoutManager =! isLinearLayoutManager // toggle layout property when menuItem is pressed
                // set layout and icon
                chooseLayout()
                setIcon(item)
                return true
            }
            else ->
                return super.onOptionsItemSelected(item) // for other items in menu, use the default event handler otherwise
        }
    }
}
