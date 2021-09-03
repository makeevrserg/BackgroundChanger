package com.makeevrserg.backgroundchanger.ui.main

import android.content.res.ColorStateList
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.makeevrserg.backgroundchanger.R
import com.makeevrserg.backgroundchanger.databinding.MainFragmentBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    val TAG = "MainFragment"

    lateinit var observer: MyLifecycleObserver


    /**
     * Получение данных после запущенных активити с выбором изображений и видео
     */
    inner class MyLifecycleObserver(private val registry: ActivityResultRegistry) :
        DefaultLifecycleObserver {
        lateinit var getVideoContent: ActivityResultLauncher<String>
        lateinit var getImageContent: ActivityResultLauncher<String>
        val TAG = "MainFragment"

        override fun onCreate(owner: LifecycleOwner) {
            getVideoContent =
                registry.register("video_key", owner, ActivityResultContracts.GetContent()) { uri ->
                    viewModel.onVideoSelected(uri)
                }
            getImageContent =
                registry.register("image_key", owner, ActivityResultContracts.GetContent()) { uri ->
                    viewModel.onBackgroundSelected(uri)
                }

        }

        fun selectVideo() = getVideoContent.launch("video/*")

        fun selectImage() = getImageContent.launch("image/*")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = MyLifecycleObserver(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.buttonSelectVideo.setOnClickListener {
            observer.selectVideo()
        }
        binding.buttonSelectBackground.setOnClickListener {
            observer.selectImage()
        }
        binding.buttonStartProc.setOnClickListener {
            viewModel.setOnStartProcessing()
        }

        viewModel.backgroundSelected.observe(viewLifecycleOwner, {
            changeButtonColor(it, binding.buttonSelectBackground)
        })

        viewModel.videoSelected.observe(viewLifecycleOwner, {
            changeButtonColor(it, binding.buttonSelectVideo)

        })

        viewModel.snackBarMessage.observe(viewLifecycleOwner, { event ->

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                event.getContentIfNotHandled() ?: return@observe,
                Snackbar.LENGTH_SHORT
            ).show()

        })

        viewModel.toastMessage.observe(viewLifecycleOwner, { event ->
            Toast.makeText(
                context,
                event.getContentIfNotHandled() ?: return@observe,
                Toast.LENGTH_SHORT
            ).show()
        })
        viewModel.showVideo.observe(viewLifecycleOwner, {
            if (it?.getContentIfNotHandled() != true)
                return@observe
            this.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToVideoFragment())
        })


        return binding.root
    }

    /**
     * Узнаем какой цвет ставить кнопке
     */
    private fun changeButtonColor(uri: Uri?, button: Button) {
        if (uri != null)
            button.setButtonProps(
                getString(R.string.video_choosed),
                R.color.colorSelected
            )
        else
            button.setButtonProps(
                getString(R.string.video_choose),
                R.color.colorSecondary
            )
    }

    /**
     * Ставим цвет и текст кнопке
     */
    private fun Button.setButtonProps(text: String, colorId: Int) {
        this.text = text
        this.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                context ?: return,
                colorId
            )
        );
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

}