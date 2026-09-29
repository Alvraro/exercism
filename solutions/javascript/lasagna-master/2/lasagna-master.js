/// <reference path="./global.d.ts" />
// @ts-check

/**
 * Implement the functions needed to solve the exercise here.
 * Do not forget to export them so they are available for the
 * tests. Here an example of the syntax as reminder:
 *
 * export function yourFunction(...) {
 *   ...
 * }
 */
export function cookingStatus(remainingTime){
  if(remainingTime === 0)
    return "Lasagna is done.";
  if(remainingTime > 0)
    return "Not done, please wait.";
  return "You forgot to set the timer.";
}

export function preparationTime(layers, layerTime = 2){
  return layers.length * layerTime;
}

export function quantities(layers){
  var noodleLayers = 0;
  var sauceLayers = 0;
  layers.forEach(function(ingredient){
    if(ingredient == "noodles")
      noodleLayers++;
    else if (ingredient == "sauce")
      sauceLayers++;
  });

  return {
    noodles: noodleLayers * 50,
    sauce: sauceLayers * 0.2,
  };
}

export function addSecretIngredient(friendsList, myList){
  myList.push(friendsList[friendsList.length - 1]);
}

export function scaleRecipe(recipe, portions){
  var newRecipe = {}
  for(let ingredient in recipe)
    newRecipe[ingredient] = (recipe[ingredient] / 2) * portions;
  return newRecipe;
}