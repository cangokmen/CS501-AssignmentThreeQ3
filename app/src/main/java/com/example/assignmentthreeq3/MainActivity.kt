package com.example.assignmentthreeq3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignmentthreeq3.ui.theme.AssignmentThreeQ3Theme
import kotlinx.coroutines.launch

// Data class to represent a single contact.
data class Contact(val name: String)

// Generates a sorted list of sample contacts.
fun populateSampleContacts(): List<Contact> {
    val names = listOf(
        "Marsha Mcneil", "Jasper Crosby", "Angelica Meyer", "Francine Lewis", "Floyd Cain",
        "Gail Gill", "Jeremy Hopkins", "Isaiah Hartman", "Aileen Stokes", "Ellsworth Burnett",
        "Lucy Blanchard", "Stefan Rich", "Vince Lyons", "Max Barnett", "Bob Costa",
        "Nathaniel Johns", "Dominique Moreno", "Curtis Downs", "Marcy Church", "Berry Hatfield",
        "Ophelia Little", "Simon Clements", "Lakisha Yang", "Ollie Travis", "Hank Adams",
        "Josie Lee", "Ivy Powell", "Jennifer Lynch", "Christoper Francis", "Rodrick Randolph",
        "Kasey Wilson", "Rowena Shah", "Ed Nixon", "Wyatt Foræd", "Deann Oconnor",
        "Jayne Frey", "Inez Shepherd", "Jeannie Ritter", "Mia Cunningham", "Lowell Mcintyre",
        "Rosetta Villarreal", "Erin Schwartz", "Santiago Robertson", "Francesca Leblanc",
        "Elvin Day", "Duane Pitts", "Wm Davila", "Gabrielle Garrett", "Tomas Ramirez",
        "Jarvis Johnson"
    )
    return names.map { Contact(it) }.sortedBy { it.name }
}

// MainActivity is the app's entry point.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Populate the contact list and set the main content view.
        val contacts = populateSampleContacts()
        setContent {
            AssignmentThreeQ3Theme {
                ContactListScreen(contacts = contacts)
            }
        }
    }
}


/**
 * Displays a contact list with alphabetical sticky headers and a "scroll to top" button.
 */
@OptIn(ExperimentalFoundationApi::class) // Required for stickyHeader.
@Composable
fun ContactListScreen(contacts: List<Contact>, modifier: Modifier = Modifier) {

    // Group contacts by the first letter of their name.
    val groupedContacts = contacts.groupBy { it.name.first().uppercaseChar() }
    // State to control and observe the scroll position of the list.
    val listState = rememberLazyListState()
    // Coroutine scope for launching the scroll animation.
    val coroutineScope = rememberCoroutineScope()

    // State to determine if the "scroll to top" button should be visible.
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 10
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            // Animate the button's appearance and disappearance.
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(onClick = {
                    // Launch an animation to scroll the list to the top.
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "scroll to top"
                    )
                }
            }
        }
    ) { innerPadding ->
        // Apply padding from Scaffold to avoid content overlapping with system bars.
        Box(modifier = Modifier.padding(innerPadding)) {
            // LazyColumn efficiently displays the scrollable list.
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                groupedContacts.forEach { (letter, contactsInGroup) ->
                    // A header that sticks to the top as the user scrolls.
                    stickyHeader {
                        Text(
                            text = letter.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    // Display the items for the current group.
                    itemsIndexed(contactsInGroup) { _, contact ->
                        Contact(contact = contact)
                    }
                }
            }
        }
    }
}


// A composable for displaying a single contact item.
@Composable
fun Contact(contact: Contact, modifier: Modifier = Modifier) {
    Text(
        text = contact.name,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        fontSize = 16.sp
    )
}


// Preview for the ContactListScreen.
@Preview(showBackground = true)
@Composable
fun ContactListScreenPreview() {
    AssignmentThreeQ3Theme {
        ContactListScreen(contacts = populateSampleContacts())
    }
}
