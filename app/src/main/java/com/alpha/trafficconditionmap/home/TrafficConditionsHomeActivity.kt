package com.alpha.trafficconditionmap.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alpha.trafficconditionmap.R
import com.alpha.trafficconditionmap.TrafficConditionsMapApp
import com.alpha.trafficconditionmap.data.model.CameraData
import com.alpha.trafficconditionmap.databinding.ActivityMainBinding
import com.alpha.trafficconditionmap.databinding.MarkerInfoBsBinding
import com.alpha.trafficconditionmap.home.di.HomeComponent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

class TrafficConditionsHomeActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: TrafficConditionMapViewModelFactory

    private lateinit var viewModel: TrafficConditionMapViewModel

    private lateinit var homeComponent: HomeComponent

    private lateinit var map: GoogleMap

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        homeComponent =
            (application as TrafficConditionsMapApp).appComponent.homeComponent().create()
        homeComponent.inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TrafficConditionMapViewModel::class.java)

        setContentView(binding.root)

        initialiseObservers()

        setupMapView()
    }

    private fun initialiseObservers() {
        viewModel.cameras.observe(this, Observer {
            addMarkers(it)
            displayMarkers(it)
        })

        viewModel.spinner.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.snackBar.observe(this, Observer {
            Snackbar.make(findViewById(R.id.parent), it, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun addMarkers(cameras: List<CameraData>) {
        cameras.forEach {
            val marker = map.addMarker(
                MarkerOptions()
                    .title(it.id)
                    .position(LatLng(it.latitude, it.longitude))
            )
            marker.tag = it
        }
    }

    private fun displayMarkers(cameras: List<CameraData>) {
        val bounds = LatLngBounds.builder()
        cameras.forEach { bounds.include(LatLng(it.latitude, it.longitude)) }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
    }

    private fun setupMapView() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync {
            it.setOnMapLoadedCallback {
                onMapReady(it)
            }
        }
    }

    private fun onMapReady(gmap: GoogleMap) {
        viewModel.fetchTrafficImageData()

        map = gmap
        map.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            uiSettings.isCompassEnabled = true
        }
            .setOnMarkerClickListener {
                val cameraData = it?.tag as? CameraData
                cameraData?.let { camera ->
                    setBottomSheet(camera)
                }
                false
            }
    }

    private fun setBottomSheet(camera: CameraData) {
        val bsBinding = MarkerInfoBsBinding.inflate(layoutInflater)
        bsBinding.bsProgress.visibility = View.VISIBLE
        Picasso.get()
            .load(camera.image)
            .into(bsBinding.image, object : Callback {
                override fun onSuccess() {
                    bsBinding.bsProgress.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    bsBinding.bsProgress.visibility = View.GONE
                }
            })
        bsBinding.timeTv.text = "Last updated at ${camera.timeStamp}"

        BottomSheetDialog(this).apply {
            setContentView(bsBinding.root)
            show()
        }
    }
}
