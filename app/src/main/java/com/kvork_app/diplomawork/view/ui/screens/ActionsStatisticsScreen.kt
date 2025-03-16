package com.kvork_app.diplomawork.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.kvork_app.diplomawork.R

@Composable
fun ActionStatisticsScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onMaterialStatistics: () -> Unit,
    onAddressStatistics: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (
            backBtnRef,
            logoRef,
            titleRef,
            subtitleRef,
            actionsTitleRef,
            addBtnRef,
            editBtnRef,
            viewBtnRef
        ) = createRefs()

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(backBtnRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_button_english),
                tint = Color.Black
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo_description),
            modifier = Modifier
                .size(64.dp)
                .constrainAs(logoRef) {
                    top.linkTo(parent.top)
                    start.linkTo(backBtnRef.end, margin = 8.dp)
                }
        )

        Text(
            text = stringResource(id = R.string.app_title),
            fontSize = 28.sp,
            color = Color(0xFF00AA00),
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(logoRef.end, margin = 12.dp)
            }
        )

        Text(
            text = stringResource(id = R.string.app_subtitle),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(subtitleRef) {
                top.linkTo(titleRef.bottom, margin = 4.dp)
                start.linkTo(titleRef.start)
            }
        )

        Text(
            text = stringResource(id = R.string.choose_action),
            fontSize = 18.sp,
            modifier = Modifier.constrainAs(actionsTitleRef) {
                top.linkTo(logoRef.bottom, margin = 100.dp)
                centerHorizontallyTo(parent)
            }
        )

        Button(
            onClick =  onMaterialStatistics,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .constrainAs(addBtnRef) {
                    top.linkTo(actionsTitleRef.bottom, margin = 24.dp)
                    centerHorizontallyTo(parent)
                }
        ) {
            Text(
                text = stringResource(id = R.string.material_stats),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }


        Button(
            onClick = onAddressStatistics,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .constrainAs(editBtnRef) {
                    top.linkTo(addBtnRef.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
        ) {
            Text(
                text = stringResource(id = R.string.address_stats),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp")
@Composable
fun ActionsStatisticsScreenPreview() {
    val navController = androidx.navigation.compose.rememberNavController()
    ActionStatisticsScreen(
        navController,
        onBackClick = {},
        onMaterialStatistics = {},
        onAddressStatistics = {}
    )

}