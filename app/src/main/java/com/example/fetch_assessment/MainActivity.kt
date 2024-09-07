package com.example.fetch_assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fetch_assessment.models.Item
import com.example.fetch_assessment.ui.theme.FetchAssessmentTheme

class MainActivity : ComponentActivity() {
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchAssessmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val items by listViewModel.items
                    val isLoading by listViewModel.isLoading
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    ItemTable(items = items)
                }
            }
        }
    }
}

@Composable
fun ItemTable(items: List<Item>) {
    // group items by id
    val groupedItems = items.groupBy { it.listId }

    Column(modifier = Modifier.fillMaxWidth()) {
        groupedItems.forEach { (listId, itemsInGroup) ->
            Group(listId = listId, items = itemsInGroup)
        }
    }
}

@Composable
fun Group(listId: Int, items: List<Item>) {
    // state to track if a group is expanded or not
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // row with list ID group, count, and icon for expanded
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "List ID: $listId",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Count: ${items.size}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            // icon to show if group is expanded or not
            Icon(
                painter = painterResource(id = if (isExpanded) android.R.drawable.arrow_up_float else android.R.drawable.arrow_down_float),
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
            )
        }

        Divider(thickness = 2.dp)

        // show group if isExpanded
        AnimatedVisibility(visible = isExpanded, ) {
            //
            Column {
                // row for columns headers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .border(1.dp, Color.Black),
                ) {
                    Text(
                        text = "listId",
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Black),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "id",
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Black),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "name",
                        modifier = Modifier
                            .weight(2f)
                            .border(1.dp, Color.Black),
                        textAlign = TextAlign.Center
                    )
                }

                // rows for data items
                LazyColumn {
                    items(items) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .border(1.dp, Color.Black),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.listId.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, Color.Black),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = item.id.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, Color.Black),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = item.name.toString(),
                                modifier = Modifier
                                    .weight(2f)
                                    .border(1.dp, Color.Black),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ItemTablePreview() {
    val items = listOf(
        Item(id = 1, listId = 2, name = "Item One"),
        Item(id = 2, listId = 2, name = "Item Two")
    )
    ItemTable(items = items)
}
