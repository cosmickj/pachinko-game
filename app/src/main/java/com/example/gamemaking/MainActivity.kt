package com.example.gamemaking

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.example.gamemaking.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isThread: Boolean? = null
    private var isClicked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        val random = Random()

        val pictureArrayList = ArrayList<Int>()
        pictureArrayList.add(R.drawable.ic_cherry)
        pictureArrayList.add(R.drawable.ic_seven)
        pictureArrayList.add(R.drawable.ic_crowns)
        pictureArrayList.add(R.drawable.ic_diamond)
        pictureArrayList.add(R.drawable.ic_bell)
        pictureArrayList.add(R.drawable.ic_four_clover)
        pictureArrayList.add(R.drawable.ic_three_clover)
        pictureArrayList.add(R.drawable.ic_grapes)

        binding.slotOne.tag = 1
        binding.slotTwo.tag = 1
        binding.slotThree.tag = 1

        var totalBettingPoint = binding.slotCurrentBetText.text.toString().toInt()
        var currentBalance = binding.slotCurrentBalanceText.text.toString().toInt()

        // 슬롯 시작 버튼
        binding.btnStart.setOnClickListener {
            if (isThread != true) {
                isThread = true

                if (totalBettingPoint > currentBalance) {
                    isThread = false
                    Toast.makeText(this, "배팅 금액을 조정해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                currentBalance -= totalBettingPoint
                binding.slotCurrentBalanceText.text = currentBalance.toString()

                binding.slotResultText.text = "두구두구두구"

                val thread = Thread {
                    while (isThread!!) {
                        val num1 = random.nextInt(pictureArrayList.size)
                        val num2 = random.nextInt(pictureArrayList.size)
                        val num3 = random.nextInt(pictureArrayList.size)
                        Thread.sleep(80)

                        handler.post {
                            binding.slotOne.tag = num1
                            binding.slotOne.setImageResource(pictureArrayList.elementAt(num1))

                            binding.slotTwo.tag = num2
                            binding.slotTwo.setImageResource(pictureArrayList.elementAt(num2))

                            binding.slotThree.tag = num3
                            binding.slotThree.setImageResource(pictureArrayList.elementAt(num3))
                        }
                    }
                }

                thread.start()
                isClicked = false
            }
        }

        // 슬롯 멈춤 버튼
        binding.btnStop.setOnClickListener {
            isThread = false

            if (isClicked == false) {
                isClicked = true

                Thread {
                    Thread.sleep(85)
                    val slotOneResult = binding.slotOne.tag
                    val slotTwoResult = binding.slotTwo.tag
                    val slotThreeResult = binding.slotThree.tag

                    val resultArrayList = listOf(slotOneResult, slotTwoResult, slotThreeResult)
                    val counts = resultArrayList.groupingBy { it }.eachCount()
                    Log.v("로그", counts.toString())

                    if (counts.filterValues { it == 3 }.keys.size == 1) // 3개 모두 같은 그림
                    {
                        when (counts.filterValues { it == 3 }.keys.elementAt(0)) {
                            0 -> handler.post {
                                binding.slotResultText.text = "체리체리체리"
                                currentBalance = timesBalance(2)
                            }
                            1 -> handler.post {
                                binding.slotResultText.text = "칠칠칠"
                                currentBalance = timesBalance(7)
                            }
                            2 -> handler.post {
                                binding.slotResultText.text = "왕관왕관왕관"
                                currentBalance = timesBalance(10)
                            }
                            3 -> handler.post {
                                binding.slotResultText.text = "다이아다이아다이아"
                                currentBalance = timesBalance(100)
                            }
                            4 -> handler.post {
                                binding.slotResultText.text = "벨벨벨"
                                currentBalance = timesBalance(29)
                            }
                            5 -> handler.post {
                                binding.slotResultText.text = "네잎네잎네잎"
                                currentBalance = timesBalance(4)
                            }
                            6 -> handler.post {
                                binding.slotResultText.text = "세잎세잎세잎"
                                currentBalance = timesBalance(3)
                            }
                            7 -> handler.post {
                                binding.slotResultText.text = "포도포도포도"
                                currentBalance = timesBalance(38)
                            }
                        }
                    } else if ((counts.filterValues { it == 2 }.keys.size == 1)) {
                        when (counts.filterValues { it == 2 }.keys.elementAt(0)) {
                            0 -> handler.post {
                                binding.slotResultText.text = "체리체리"
                                currentBalance = plusBalance(2)
                            }
                            1 -> handler.post {
                                binding.slotResultText.text = "칠칠"
                                currentBalance = plusBalance(7)
                            }
                            2 -> handler.post {
                                binding.slotResultText.text = "왕관왕관"
                                currentBalance = plusBalance(10)
                            }
                            3 -> handler.post {
                                binding.slotResultText.text = "다이아다이아"
                                currentBalance = plusBalance(100)
                            }
                            4 -> handler.post {
                                binding.slotResultText.text = "벨벨"
                                currentBalance = plusBalance(29)
                            }
                            5 -> handler.post {
                                binding.slotResultText.text = "네잎네잎"
                                currentBalance = plusBalance(4)
                            }
                            6 -> handler.post {
                                binding.slotResultText.text = "세잎세잎"
                                currentBalance = plusBalance(3)
                            }
                            7 -> handler.post {
                                binding.slotResultText.text = "포도포도"
                                currentBalance = plusBalance(38)
                            }
                        }
                    } else {
                        handler.post { binding.slotResultText.text = "다시 도전하세요" }
                    }

                }.start()

                Thread {
                    Thread.sleep(90)
                    if (currentBalance <= 0) {
                        isClicked = false
                        val intent = Intent(this, MainActivity::class.java)

                        val builder = AlertDialog.Builder(this)
                        handler.post {
                            builder.setMessage("Game Over")
                                .setPositiveButton(
                                    "Restart",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        startActivity(intent)
                                        finish()
                                    })
                            builder.show()
                        }
                    }
                }.start()
            }
        }

        // 배팅 포인트 증가
        binding.btnBetUp.setOnClickListener {
            when (totalBettingPoint) {
                in 10..40 -> totalBettingPoint += 10
                in 1..9 -> totalBettingPoint += 1
            }
            binding.slotCurrentBetText.text = totalBettingPoint.toString()
        }

        // 배팅 포인트 감소
        binding.btnBetDown.setOnClickListener {
            when (totalBettingPoint) {
                in 20..50 -> totalBettingPoint -= 10
                in 2..10 -> totalBettingPoint -= 1
            }
            binding.slotCurrentBetText.text = totalBettingPoint.toString()
        }

        Thread {
            while (true) {
                Thread.sleep(3000)
                handler.post {
                    if (binding.fireworksAnimation.isVisible) // visible
                        binding.fireworksAnimation.visibility = View.GONE
                    else
                        binding.fireworksAnimation.visibility = View.VISIBLE
                }
            }
        }.start()
    }

    fun plusBalance(point: Int): Int {
        var totalBettingPoint = binding.slotCurrentBetText.text.toString().toInt()
        var currentBalance = binding.slotCurrentBalanceText.text.toString().toInt()

        totalBettingPoint += point
        currentBalance += totalBettingPoint
        binding.slotCurrentBalanceText.text = currentBalance.toString()
        return currentBalance
    }

    fun timesBalance(point: Int): Int {
        var totalBettingPoint = binding.slotCurrentBetText.text.toString().toInt()
        var currentBalance = binding.slotCurrentBalanceText.text.toString().toInt()

        totalBettingPoint *= point
        currentBalance += totalBettingPoint
        binding.slotCurrentBalanceText.text = currentBalance.toString()
        return currentBalance
    }

}