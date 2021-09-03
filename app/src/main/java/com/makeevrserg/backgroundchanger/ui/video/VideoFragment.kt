package com.makeevrserg.backgroundchanger.ui.video

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.makeevrserg.backgroundchanger.R
import com.makeevrserg.backgroundchanger.databinding.MainFragmentBinding
import com.makeevrserg.backgroundchanger.databinding.VideoFragmentBinding
import com.makeevrserg.backgroundchanger.ui.main.MainViewModel

class VideoFragment : Fragment() {

    private val viewModel: VideoViewModel by lazy {
        ViewModelProvider(this).get(VideoViewModel::class.java)
    }

    /**
     * Прячем ActionBar чтобы видео было на весь экран
     */
    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: VideoFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.video_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.player.observe(viewLifecycleOwner, {
            binding.player.player = it
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


}