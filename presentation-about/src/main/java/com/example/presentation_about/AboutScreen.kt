package com.example.presentation_about

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // КемГУ))))
    val officePoint = Point(55.351912, 86.091051)

    val mapView = remember { MapView(context) }

    // Управление жизненным циклом MapView (обязательное требование Яндекс Карт)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("ООО «Чёто разрабатываем (вроде)»", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Надеемся Вам понравился наш To-Do list, но если хотите с нами сотрудничать, лучше закажите Web-приложение <3",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Адрес: г. Кемерово, ул. Красная, д. 6", fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(16.dp))

        // Яндекс Карта
        Card(modifier = Modifier.fillMaxWidth().weight(1f)) {
            AndroidView(
                factory = {
                    mapView.apply {
                        mapWindow.map.move(CameraPosition(officePoint, 15f, 0f, 0f))

                        val imageProvider = ImageProvider.fromResource(context, R.drawable.ic_location_pin)
                        mapWindow.map.mapObjects.addPlacemark(officePoint, imageProvider)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка построения маршрута
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                try {
                    // Пытаемся открыть маршрут в приложении Яндекс Карт
                    val yandexUri = Uri.parse("yandexmaps://build_route_on_map?lat_to=${officePoint.latitude}&lon_to=${officePoint.longitude}")
                    val intent = Intent(Intent.ACTION_VIEW, yandexUri)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Если Яндекс Карты не установлены, открываем любой системный навигатор (Google, 2GIS и т.д.)
                    try {
                        val genericUri = Uri.parse("geo:${officePoint.latitude},${officePoint.longitude}?q=${officePoint.latitude},${officePoint.longitude}(Наш+офис)")
                        val intent = Intent(Intent.ACTION_VIEW, genericUri)
                        context.startActivity(intent)
                    } catch (ex: Exception) {
                        Toast.makeText(context, "Нет приложения для просмотра карт :(", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Route")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Построить маршрут")
        }
    }
}