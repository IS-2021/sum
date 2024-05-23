<script setup lang="ts">
import { computed, ref } from 'vue';
import type { AutocompleteDTO } from '@/lib/api-model';

import { Check, ChevronsUpDown, LoaderCircleIcon } from 'lucide-vue-next';

import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';

interface CitiesComboboxProps {
  completions: AutocompleteDTO[];
}

const props = defineProps<CitiesComboboxProps>();

const open = ref(false);
const value = ref('');

const emit = defineEmits<{
  searchChange: [payload: string];
}>();

const citiesKey = computed(() => props.completions.map((city) => city.description).join(''));
</script>

<template>
  <Popover v-model:open="open">
    <PopoverTrigger as-child>
      <Button
        variant="outline"
        role="combobox"
        :aria-expanded="open"
        class="w-[200px] justify-between"
      >
        {{
          value
            ? props.completions.find((completion) => completion.placeId === value)?.description
            : 'Search addresses...'
        }}
        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
      </Button>
    </PopoverTrigger>
    <PopoverContent class="w-[200px] p-0">
      <Command :key="citiesKey">
        <CommandInput
          class="h-9"
          placeholder="Search addresses..."
          v-on:input="(e) => emit('searchChange', e.target.value)"
        />
        <CommandEmpty class="grid justify-center">
          <LoaderCircleIcon class="animate-spin" />
        </CommandEmpty>
        <CommandList>
          <CommandGroup>
            <CommandItem
              v-for="completion in completions"
              :key="completion.placeId"
              :value="completion.placeId"
              @select="
                (ev) => {
                  if (typeof ev.detail.value === 'string') {
                    value = ev.detail.value;
                  }
                  open = false;
                }
              "
            >
              {{ completion.description }}
              <Check
                :class="
                  cn('ml-auto h-4 w-4', value === completion.placeId ? 'opacity-100' : 'opacity-0')
                "
              />
            </CommandItem>
          </CommandGroup>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
</template>
