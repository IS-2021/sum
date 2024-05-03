<script setup lang="ts">
import { Check, ChevronsUpDown } from 'lucide-vue-next'

import { ref } from 'vue'
import { cn } from '@/lib/utils'
import { Button } from '@/components/ui/button'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList
} from '@/components/ui/command'
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover'

import { useGetIngredients } from '@/lib/api/ingredients/ingredients';
import { unref } from 'vue';
import type { Uuid } from '@/lib/api-model';
import type { Ref } from 'vue'

interface Ingredient {
  id: Uuid,
  name: string,
  type: string
}

const { data } = useGetIngredients();
let ingredientsData = unref(data)?.data;
const parsedIngredients: Ref<Ingredient[] | undefined> = ref(JSON.parse(JSON.stringify(ingredientsData)));
const ingredients = parsedIngredients.value?.filter((item, index) => parsedIngredients.value?.indexOf(item) === index)
const filtered: Ref<Ingredient[] | undefined> = ref(ingredients);

const open = ref(false)

function updateFilter(element: Ingredient) {
  if (filtered.value?.find(e => e.name === element.name) !== undefined) {
    const index = filtered.value?.indexOf(element);
    filtered.value.splice(index, 1);
  }
  else {
    filtered.value?.push(element);
  }
  console.log(filtered.value)
}

</script>

<template>
  <Popover v-model:open="open">
    <PopoverTrigger as-child>
      <Button variant="outline" role="combobox" :aria-expanded="open" class="w-full justify-between text-white">
        Select unwanted ingredients...

        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50 " />
      </Button>
    </PopoverTrigger>
    <PopoverContent class="w-full p-0">
      <Command>
        <CommandInput placeholder="Search ingredients..." />
        <CommandEmpty>No ingredients found.</CommandEmpty>
        <CommandList>
          <CommandGroup>
            <CommandItem v-for="ingredient in ingredients" :key="ingredient.id" :value="ingredient" :id="ingredient.id"
              @select="open = true" @click.stop="updateFilter(ingredient)">
              <Check :class="cn(
                'mr-2 h-4 w-4',
                filtered?.find((e) => e.name === ingredient.name) ? 'opacity-100' : 'opacity-0'
              )" />
              {{ ingredient.name }}
            </CommandItem>
          </CommandGroup>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
</template>
