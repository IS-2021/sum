<script setup lang="ts">
import { ref, unref, type Ref } from 'vue';

import { Check, ChevronsUpDown } from 'lucide-vue-next';
import { cn } from '@/lib/utils';

import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import Button from '@/components/ui/button/Button.vue';
import { Plus } from 'lucide-vue-next';

import { postIngredients, useGetIngredients } from '@/lib/api/ingredients/ingredients';
import type { IngredientDTO, Uuid } from '@/lib/api-model';

const props = defineProps<{
  mealId: Uuid;
  userId: Uuid;
}>();

const open = ref(false);

const { data } = useGetIngredients();
const ingredients = unref(data)?.data;
const chosenIngredients: Ref<IngredientDTO[]> = ref([]);

function addIngredient(ingredient: IngredientDTO) {
  chosenIngredients.value.push(ingredient);
}

function postChosenIngredients() {
  chosenIngredients.value.forEach((i) => {
    postIngredients(
      { name: i.name, type: i.type },
      { mealId: props.mealId, restaurantId: props.userId },
    );
  });
}
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button>
        <Plus width="16" height="16" class="mr-1" />
        Add Ingredients
      </Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle class="pb-4">
          Add ingredients
          <Popover v-model:open="open">
            <PopoverTrigger as-child>
              <Button
                variant="outline"
                role="combobox"
                :aria-expanded="open"
                class="w-full justify-between mt-4"
              >
                Select ingredients...

                <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
              </Button>
            </PopoverTrigger>
            <PopoverContent class="w-full p-0">
              <Command>
                <CommandInput placeholder="Search ingredients..." />
                <CommandEmpty>No ingredients found.</CommandEmpty>
                <CommandList>
                  <CommandGroup>
                    <CommandItem
                      v-for="ingredient in ingredients"
                      :key="ingredient.ingredientId"
                      :value="ingredient"
                      :id="ingredient.ingredientId"
                      @select="open = true"
                      @click="addIngredient(ingredient)"
                    >
                      <Check
                        :class="
                          cn(
                            'mr-2 h-4 w-4',
                            chosenIngredients?.find((e) => e.name === ingredient.name)
                              ? 'opacity-100'
                              : 'opacity-0',
                          )
                        "
                      />
                      {{ ingredient.name }}
                    </CommandItem>
                  </CommandGroup>
                </CommandList>
              </Command>
            </PopoverContent>
          </Popover>
        </DialogTitle>
      </DialogHeader>

      <DialogHeader>
        <DialogTitle> </DialogTitle>
      </DialogHeader>

      <DialogFooter class="sm:justify-start">
        <DialogClose as-child>
          <Button type="button" variant="secondary" @click="postChosenIngredients()"
            >Add ingredients</Button
          >
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
