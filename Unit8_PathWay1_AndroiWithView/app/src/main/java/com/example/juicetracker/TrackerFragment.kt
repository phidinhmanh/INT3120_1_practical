/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.juicetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.juicetracker.databinding.FragmentTrackerBinding
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.JuiceListAdapter
import com.example.juicetracker.ui.TrackerViewModel
import kotlinx.coroutines.launch

class TrackerFragment : Fragment() {

    private val viewModel by viewModels<TrackerViewModel> { AppViewModelProvider.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTrackerBinding.inflate(inflater, container, false).root
    }

    private val adapter = JuiceListAdapter(
        onEdit = { drink ->
        findNavController().navigate(
            TrackerFragmentDirections.actionTrackerFragmentToEntryDialogFragment(drink.id)
        )
        },
        onDelete = { drink ->
            viewModel.deleteJuice(drink)
        }
    )

    /**
     * Called after the view has been created, and the fragment's view lifecycle
     * is at least in the [Lifecycle.State.CREATED] state.
     *
     * This function sets up the UI by binding the view, setting up the adapter
     * for the RecyclerView, and setting up an OnClickListener for the FloatingActionButton.
     *
     * The Observer for the FloatingActionButton is currently a no-op, but it could be
     * used to launch a UI for adding a new juice entry.
     *
     * The function also sets up a coroutine to collect the juice entries from the
     * [TrackerViewModel]'s [juicesStream] and submit them to the adapter.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from
     * a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentTrackerBinding.bind(view)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                TrackerFragmentDirections.actionTrackerFragmentToEntryDialogFragment()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.juicesStream.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}
