package com.example.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var currentNumber = ""
    private var storedNumber: Double? = null
    private var currentOp: String? = null
    private var newInput = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val numberButtons = listOf(
            binding.button to "0",
            binding.button12 to "1",
            binding.button13 to "2",
            binding.button14 to "3",
            binding.button18 to "4",
            binding.button19 to "5",
            binding.button20 to "6",
            binding.button15 to "7",
            binding.button16 to "8",
            binding.button17 to "9"
        )
        for ((button, numStr) in numberButtons) {
            button.setOnClickListener {
                if (newInput) {
                    currentNumber = numStr
                    newInput = false
                } else {
                    currentNumber += numStr
                }
                binding.textView.text = currentNumber
            }
        }

        binding.button21.setOnClickListener {
            if (currentNumber.isNotEmpty() && !newInput) {
                currentNumber = currentNumber.dropLast(1)
                if (currentNumber.isEmpty()) {
                    binding.textView.text = "0"
                    newInput = true
                } else {
                    binding.textView.text = currentNumber
                }
            }
        }




        binding.button9.setOnClickListener { onOperatorPressed("+") }
        binding.button11.setOnClickListener { onOperatorPressed("-") }
        binding.button8.setOnClickListener { onOperatorPressed("*") }
        binding.button10.setOnClickListener { onOperatorPressed("/") }
        binding.button22.setOnClickListener { calculateResult() }

    }

    private fun onOperatorPressed(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (storedNumber == null) {
                storedNumber = currentNumber.toDoubleOrNull() ?: 0.0
            } else if (currentOp != null) {
                // Önceki işlemi yap
                val right = currentNumber.toDoubleOrNull() ?: 0.0
                storedNumber = when (currentOp) {
                    "+" -> storedNumber!! + right
                    "-" -> storedNumber!! - right
                    "*" -> storedNumber!! * right
                    "/" -> if (right != 0.0) storedNumber!! / right else Double.NaN
                    else -> right
                }
                binding.textView.text = storedNumber.toString()
            }
            currentNumber = ""
        }
        currentOp = op
        newInput = true
    }

    private fun calculateResult() {
        if (currentOp != null && currentNumber.isNotEmpty()) {
            val right = currentNumber.toDoubleOrNull() ?: 0.0
            val left = storedNumber ?: 0.0
            val result = when (currentOp) {
                "+" -> left + right
                "-" -> left - right
                "*" -> left * right
                "/" -> if (right != 0.0) left / right else Double.NaN
                else -> right
            }
            currentNumber = result.toString()
            binding.textView.text = currentNumber
            storedNumber = null
            currentOp = null
            newInput = true
        }
    }
}