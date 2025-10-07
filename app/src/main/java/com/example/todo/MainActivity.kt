package com.example.todo

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.activity.*
import kotlinx.serialization.*
import androidx.compose.ui.unit.*


import kotlinx.serialization.json.Json
import android.content.Context
import java.io.File
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.todo.ui.theme.ToDoTheme
import androidx.compose.ui.text.TextStyle
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb


//Hauptprogramm
class MainActivity : ComponentActivity() {

    //Variablen
    val tasks = mutableStateListOf<Task>()

    //Initialisierung
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Hauptseite aufrufen
        setContent {
            ToDoTheme {
                Log.d("MyTag", "App getsartet")
                tasks.addAll(loadTasks(this))
                MainScreen(tasks)
            }
        }
    }

    //Hauptseite
    @Composable
    fun MainScreen(tasks: MutableList<Task>) {
        var showDialog by remember { mutableStateOf(false) }

        //Grundgerüst
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            //Button für neue Aufgaben
            floatingActionButton = {
                CreateTaskButton(onClick = { showDialog = true })
            }
        ) { innerPadding ->
            /*Eine Spalte mit mehreren Zeilen für Layout

                Banner
                Taskleiste
                    -Aufgabe1
                    -Aufgabe2
                    ...

             */
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Banner()
                Tasklist(
                    tasks = tasks,
                )
            }

            //showDialog: true wenn Button gedrückt, falsch wenn Fenster verlassen um Menü anzuzeigen
            if (showDialog) {
                AddTaskDialog(
                    tasks = tasks,
                    onDismiss = { showDialog = false }
                )
            }


        }
    }

    //Anzeigen Banner oben
    @Composable
    fun Banner() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(16.dp)
        ) {
            Text(
                text = "ToDo",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    //Anzeigen neue Aufagebe Button
    @Composable
    fun CreateTaskButton(onClick: () -> Unit) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Neue Aufgabe"
            )
        }
    }

    //Anzeigen neue Aufgabe erstellen Menü
    @Composable
    fun AddTaskDialog(tasks: MutableList<Task>, onDismiss: () -> Unit) {

        //Variable die neue Aufgaben speichert
        var text by remember { mutableStateOf("") }
        var selectedColor = Color(0xFFE56E90)

        //Das Aufgabenmenü
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(
                text = "Neue Aufgabe",
                color = Color.White
                ) },
            containerColor = Color.DarkGray,
            //Inhalt: Eingabezeile + Farbauswahl
            text = {
                Column{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .padding(12.dp)
                    ){
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = { Text(
                                text = "Aufgabe eingeben",
                                color = Color.White
                            ) },
                            textStyle = TextStyle(color = Color.White)

                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .padding(20.dp)
                    ){
                    Row{
                        //Rot
                        Button(
                            onClick = {
                                Log.d("MyTag", "Roter Button")
                                selectedColor = Color(0xFFE56E90)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE56E90),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)

                        ) {}

                        Spacer(modifier = Modifier.width(4.dp))

                        //Gelb
                        Button(
                            onClick = {
                                Log.d("MyTag", "Gelber Button")
                                selectedColor = Color(0xFFE7D27C)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE7D27C),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {}

                        Spacer(modifier = Modifier.width(4.dp))

                        //Grün
                        Button(
                            onClick = {
                                Log.d("MyTag", "Grüner Button")
                                selectedColor = Color(0xFF9DD6AD)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9DD6AD),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {}

                        Spacer(modifier = Modifier.width(4.dp))

                        //Blau
                        Button(
                            onClick = {
                                Log.d("MyTag", "Blauer Button")
                                selectedColor = Color(0xFFB2C9E2)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB2C9E2),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {}
                    }
                    }
                }

            },
            confirmButton = {
                Button(onClick = {
                    Log.d("MyTag", text)

                    if (text != "") {
                        tasks.add(Task(text = text, color = selectedColor.toArgb(), isDone = false))
                        tasks.sortBy { it.isDone }

                        //Toggle damit die Anzeige aktualisiert wird
                        val toggle = Task(text = "", color = Color(0xF5492700).toArgb(), isDone = false)
                        tasks.add(toggle)
                        tasks.remove(toggle)
                        saveTasks(context = this@MainActivity, tasks = tasks)

                    }
                    onDismiss()
                }) {
                    Text("Hinzufügen")
                }
            }
        )
    }

    //Anzeigen der Aufgaben
    @Composable
    fun Tasklist(tasks: MutableList<Task>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 8.dp)
        ) {
            //Aufgaben anzeigen
            Log.d("MyTag", tasks.toString())
            for (i in tasks.indices) {

                item {
                    Row {
                        Checkbox(

                            checked = tasks[i].isDone,

                            onCheckedChange = { checked ->
                                tasks[i].isDone = checked
                                tasks.sortBy { it.isDone }

                                //Toggle damit die Anzeige aktualisiert wird
                                val toggle = Task(text = "", color = Color(0xF5492700).toArgb(), isDone = false)
                                tasks.add(toggle)
                                tasks.remove(toggle)
                                saveTasks(context = this@MainActivity, tasks = tasks)

                                Log.d("MyTag", tasks.toString())
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.White,
                                uncheckedColor = Color.LightGray
                            )
                        )
                        var showDelete by remember { mutableStateOf(false) }

                        Button(
                            onClick = {
                                showDelete = true
                                Log.d("MyTag", "Blauer Button")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(tasks[i].color),
                                contentColor = Color.DarkGray
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .background(Color(tasks[i].color), shape = RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                tasks[i].text,
                                fontSize = 18.sp
                            )
                        }

                        if (showDelete){
                            DeleteMenue(task = tasks[i], onDismiss = { showDelete = false })
                        }
                    }
                }
            }
        }
    }

    //Menü Aufgaben löschen
    @Composable
    fun DeleteMenue(
        task: Task,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Aufgabe löschen?",
                    color = Color.White
                )
            },
            containerColor = Color.DarkGray,
            text = {
                var text by remember { mutableStateOf(task.text) }
                Column {
                    OutlinedTextField(value = text,
                        onValueChange = {
                            text = it
                            task.text = it
                                        },
                        textStyle = TextStyle(color = Color.White))

                    saveTasks(context = this@MainActivity, tasks = tasks)

                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Speichern")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        tasks.remove(task)
                        saveTasks(context = this@MainActivity, tasks = tasks)
                        onDismiss()
                    }
                ) {
                    Text("löschen")
                }

            }
        )
    }

}


//Objekt um Aufgabene zu speichern
@Serializable
data class Task(
    var text: String,
    var color: Int,
    var isDone: Boolean = false
)

//Aufgaben speichern
fun saveTasks(context: Context, tasks: List<Task>) {
    val json = Json.encodeToString(tasks)
    val file = File(context.filesDir, "tasks.json")
    file.writeText(json)
}

//Aufgaben aus dem Speicher laden
fun loadTasks(context: Context): MutableList<Task> {
    val file = File(context.filesDir, "tasks.json")
    if (!file.exists()) return mutableListOf()

    val json = file.readText()
    return Json.decodeFromString(json)
}
