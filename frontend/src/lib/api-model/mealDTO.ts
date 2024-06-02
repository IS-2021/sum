/**
 * Generated by orval v6.27.1 🍺
 * Do not edit manually.
 * Sumatywny
 * OpenAPI spec version: 1.0.0
 */
import type { IngredientDTO } from './ingredientDTO';
import type { Uuid } from './uuid';

export interface MealDTO {
  description: string;
  /** List of ingredients */
  ingredients: IngredientDTO[];
  mealId: Uuid;
  name: string;
  restaurantId: Uuid;
}
