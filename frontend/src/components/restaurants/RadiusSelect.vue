<script setup lang="ts">
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

const radiusModel = defineModel<string | undefined>('radius');
</script>

<template>
  <Select v-model="radiusModel" default-value="5">
    <SelectTrigger class="w-[180px]">
      <SelectValue placeholder="Select radius" />
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectLabel>Restaurants within</SelectLabel>
        <SelectItem value="2"> 2 km </SelectItem>
        <SelectItem value="5"> 5 km </SelectItem>
        <SelectItem value="10"> 10 km </SelectItem>
        <SelectItem value="15"> 15 km </SelectItem>
        <SelectItem value="20"> 20 km </SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>
