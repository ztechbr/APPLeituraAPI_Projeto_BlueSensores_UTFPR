package io.ztechnologies.monitorsensores.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.ztechnologies.monitorsensores.data.remote.RetrofitClient
import io.ztechnologies.monitorsensores.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupDefaultDates()
        setupListeners()
    }

    private fun setupDefaultDates() {
        val calendar = Calendar.getInstance()
        val dateFim = dateFormatter.format(calendar.time)
        
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val dateInicio = dateFormatter.format(calendar.time)

        binding.etDataInicio.setText(dateInicio)
        binding.etDataFim.setText(dateFim)
    }

    private fun setupListeners() {
        binding.etDataInicio.setOnClickListener { 
            showDatePicker(binding.etDataInicio.text.toString()) { date -> 
                binding.etDataInicio.setText(date) 
            } 
        }
        binding.etDataFim.setOnClickListener { 
            showDatePicker(binding.etDataFim.text.toString()) { date -> 
                binding.etDataFim.setText(date) 
            } 
        }

        binding.btnFetch.setOnClickListener {
            val token = binding.etToken.text.toString().trim()
            val codPlantacao = binding.etCodPlantacao.text.toString().trim()
            val dataInicio = binding.etDataInicio.text.toString().trim()
            val dataFim = binding.etDataFim.text.toString().trim()
            val limit = binding.etLimit.text.toString().toIntOrNull() ?: 50
            val offset = binding.etOffset.text.toString().toIntOrNull() ?: 0

            if (token.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o Token de Autorização", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Define o token globalmente antes de mudar de tela
            RetrofitClient.setToken(token)

            // Abre a tela de resultados passando os filtros
            val intent = Intent(this, ResultsActivity::class.java).apply {
                putExtra("EXTRA_COD_PLANTACAO", codPlantacao.ifEmpty { null })
                putExtra("EXTRA_DATA_INICIO", dataInicio.ifEmpty { null })
                putExtra("EXTRA_DATA_FIM", dataFim.ifEmpty { null })
                putExtra("EXTRA_LIMIT", limit)
                putExtra("EXTRA_OFFSET", offset)
            }
            startActivity(intent)
        }
    }

    private fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        try {
            dateFormatter.parse(currentDate)?.let {
                calendar.time = it
            }
        } catch (e: Exception) { }

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormatter.format(selectedCalendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
