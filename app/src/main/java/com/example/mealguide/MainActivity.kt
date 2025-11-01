package com.example.mealguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mealguide.ui.theme.CadetGray
import com.example.mealguide.ui.theme.Mantis
import com.example.mealguide.ui.theme.MealGuideTheme
import com.example.mealguide.ui.theme.Onyx
import com.example.mealguide.ui.theme.Onyx2


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealGuideTheme {
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                Scaffold(
                    topBar = {
                        if ("recipes" == backStackEntry.value?.destination?.route) {
                        TopBar("Recipes")
                        }
                        if ("shopping" == backStackEntry.value?.destination?.route){
                            TopBar("Shopping List")
                        }
                             },
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem("Home", "home", Icons.Default.Home),
                                BottomNavItem("Recipes", "recipes", Icons.Default.Star),
                                BottomNavItem("Shopping", "shopping", Icons.Default.ShoppingCart)
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .background(Onyx2)
                        .padding(innerPadding)) {
                        Navigation(navController)
                    }
                }
            }
        }
    }
}
@Composable
fun Navigation(navController : NavHostController) {
    val dayViewModel: DayViewModel = viewModel()
    val recipeViewModel: RecipeViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen( navController, dayViewModel)
        }
        composable("recipes") {
            RecipeScreen(recipeViewModel)
        }
        composable("shopping") {
            ShoppingScreen(dayViewModel)
        }
        composable(route = "day/{dayName}",
            arguments = listOf(navArgument("dayName") { type = NavType.StringType })
        ) { backStackEntry ->
            val dayName = backStackEntry.arguments?.getString("dayName") ?: "Unknown"
            DayScreen(dayName, navController, dayViewModel, recipeViewModel)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(screenName : String){
    CenterAlignedTopAppBar(
        title = { Text(screenName) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Onyx,
            titleContentColor = Color.White
        )
    )
}
@Composable
fun BottomNavigationBar(
    items : List<BottomNavItem>,
    navController : NavController,
    onItemClick: (BottomNavItem) -> Unit
){
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar (
        containerColor = Onyx
    ){
        items.forEach { item->
            val selected =  item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Mantis,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                icon = {
                    Column (horizontalAlignment = CenterHorizontally) {
                        Icon(imageVector = item.icon,
                            contentDescription = item.name)
                        if (selected){
                            Text(item.name)
                        }
                    }
                }
            )
        }
    }

}

@Composable
fun HomeScreen( navController : NavController, dayViewModel: DayViewModel){
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item{
            Text("YOUR MEALS",
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                color = Color.White,
                modifier = Modifier.
                padding(top = 20.dp))
        }
        item{
            Spacer(Modifier.height(30.dp))
        }
        items (dayViewModel.days){ day ->
                DayCard(day, navController)
            }
    }
}

@Composable
fun DayCard(day : DayCardItem, navController: NavController){
    Column (modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .border(3.dp, Mantis, RoundedCornerShape(15.dp))
        .background(CadetGray, shape = RoundedCornerShape(15.dp))
        .padding(8.dp)
        .clickable(onClick = { navController.navigate("day/${day.name}") }),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Text(day.name, fontSize = 20.sp)
        var meal = day.recipe?.name
        if (meal == null) {
            meal = "No meal planned"
        }
        Text(meal)
    }
}

@Composable
fun DayScreen(day: String,
              navController: NavController,
              dayViewModel: DayViewModel,
              recipeViewModel: RecipeViewModel,
              ){
    Column (modifier = Modifier
        .fillMaxSize()
        .background(CadetGray),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(Onyx),
            contentAlignment = Alignment.Center){
            Text (day, color = Color.White, fontSize = 30.sp)
            IconButton(onClick = {navController.popBackStack()},
                modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(imageVector = Icons.Default.Close,
                    contentDescription = "Back",
                    tint = Color.White)
            }
        }
        LazyColumn (modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .background(CadetGray)
            .padding(20.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
            items(recipeViewModel.recipes){recipe->
                RecipeListItem(recipe,
                    isSelected = recipeViewModel.currentRecipe.value == recipe,
                    onClick = {recipeOnClick(recipe, recipeViewModel)})
            }
        }
        Button(onClick = {
            if (recipeViewModel.currentRecipe.value != null){
                dayViewModel.updateDayRecipe(day, recipeViewModel.currentRecipe.value!!)
                navController.popBackStack()
            }
        }
        ) {
            Text("Plan Meal")
        }
    }
}
@Composable
fun RecipeScreen(recipeViewModel: RecipeViewModel){
    var myRecipes by remember { mutableStateOf(true) }

    Column (modifier = Modifier
        .background(CadetGray)
        .fillMaxSize()
    ){
        Row(modifier = Modifier
            .background(Onyx2)
            .fillMaxWidth(),
        ){
            Button(modifier = Modifier
                .fillMaxWidth(.5f)
                .border(1.dp, Color.White),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                onClick= {myRecipes = true}) {Text("My Recipes") }
            Button(modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                onClick = {myRecipes = false}) {Text("New Recipe") }
        }
        if (myRecipes){
            MyRecipes(recipeViewModel)
        }
        else {
            NewRecipe(recipeViewModel)
        }
    }
}

fun recipeOnClick(recipe: Recipe, recipeViewModel: RecipeViewModel){
    if (recipeViewModel.currentRecipe.value == recipe){
        recipeViewModel.currentRecipe.value = null
    }
    else{
        recipeViewModel.currentRecipe.value = recipe
    }
}

@Composable
fun NewRecipe(recipeViewModel: RecipeViewModel){
    var recipeName by remember { mutableStateOf("") }
    var recipeIngredient by remember { mutableStateOf("") }
    val recipeIngredients = remember { mutableStateListOf<String>() }
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(15.dp),
        horizontalAlignment = CenterHorizontally){
        TextField(value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Recipe Name", color = Onyx) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true)
        TextField(value = recipeIngredient,
            onValueChange = {recipeIngredient = it},
            label = { Text("Ingredient", color = Onyx) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true)
        Button(onClick = {if (recipeIngredient != ""){
            recipeIngredients.add(recipeIngredient)
            recipeIngredient = ""
            }
        }) {
            Text("Add ingredient")
        }
        LazyColumn (modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(5.dp,Onyx2, RoundedCornerShape(5.dp))
            .padding(10.dp)) {
           items(recipeIngredients) { ingredient ->
                Text(ingredient) }
        }
        Button(onClick = {
            if (recipeName != "") {
                recipeViewModel.addRecipe(Recipe(recipeName, recipeIngredients.toList()))
                recipeName = ""
                recipeIngredients.clear()
            }
        }) {
            Text("Create new recipe")
        }
    }
}
@Composable
fun MyRecipes(recipeViewModel: RecipeViewModel){
    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)){
        items(recipeViewModel.recipes){recipe->
            RecipeListItem(recipe,
                isSelected = recipeViewModel.currentRecipe.value == recipe,
                onClick = {recipeOnClick(recipe, recipeViewModel)})
        }
    }
}
@Composable
fun RecipeListItem(recipe: Recipe, isSelected : Boolean, onClick: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .border(3.dp, Onyx, RoundedCornerShape(15.dp))
        .padding(10.dp)
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        LazyColumn (modifier = Modifier.heightIn(max = 200.dp)) {
            item { Text(recipe.name)  }

            if (isSelected) {
                items(recipe.ingredients) { ingredient->
                    Text(ingredient)
                }
            }
        }
    }
}
@Composable
fun ShoppingScreen(dayViewModel: DayViewModel){
    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .background(CadetGray)) {
        items(dayViewModel.getUniqueIngredients().toList()){ ingredient ->
            Text(ingredient)
        }
    }
}


@Preview
@Composable
fun Preview() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {if ("recipes" == backStackEntry.value?.destination?.route)
        {
            TopBar("Recipes")
        } },
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem("Home", "home", Icons.Default.Home),
                    BottomNavItem("Recipes", "recipes", Icons.Default.Star),
                    BottomNavItem("Shopping", "shopping", Icons.Default.ShoppingCart)
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .background(Onyx2)
            .padding(innerPadding)) {
            Navigation(navController)
        }
    }
}




