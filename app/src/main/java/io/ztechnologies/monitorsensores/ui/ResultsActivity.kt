package io.ztechnologies.monitorsensores.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.ztechnologies.monitorsensores.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private val viewModel: SensorViewModel by viewModels()
    private val adapter = SensorAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        
        handleIntent()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        binding.rvSensors.layoutManager = LinearLayoutManager(this)
        binding.rvSensors.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.sensors.observe(this) { sensors ->
            if (sensors.isNullOrEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                adapter.submitList(emptyList())
            } else {
                binding.tvEmptyState.visibility = View.GONE
                
                // Converte a lista de dados para itens do adapter (Header + Itens)
                val items = mutableListOf<ResultItem>()
                items.add(ResultItem.Header(sensors))
                items.addAll(sensors.map { ResultItem.Sensor(it) })
                
                adapter.submitList(items)
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun handleIntent() {
        val codPlantacao = intent.getStringExtra("EXTRA_COD_PLANTACAO")
        val dataInicio = intent.getStringExtra("EXTRA_DATA_INICIO")
        val dataFim = intent.getStringExtra("EXTRA_DATA_FIM")
        val limit = intent.getIntExtra("EXTRA_LIMIT", 50)
        val offset = intent.getIntExtra("EXTRA_OFFSET", 0)

        viewModel.fetchSensors(
            codPlantacao = codPlantacao,
            dataLeitInicio = dataInicio,
            dataLeitFim = dataFim,
            limit = limit,
            offset = offset
        )
    }
}
