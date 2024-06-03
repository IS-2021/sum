<script setup lang="ts">
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { formFields } from '@/components/(manage)/common/fields/fields';
</script>

<template>
  <div class="grid max-w-80 grid-cols-[max-content,1fr,1fr] gap-2 gap-x-4">
    <p class="col-start-2 text-sm font-medium">Open</p>
    <p class="text-sm font-medium">Close</p>

    <template v-for="{ label, fieldName } in formFields.hours" :key="fieldName">
      <p aria-hidden="true" class="h-fit self-center">
        {{ label }}
      </p>

      <FormField v-slot="{ componentField }" :name="`${fieldName}[0]`">
        <FormItem class="space-y-0">
          <FormLabel class="sr-only">Open</FormLabel>
          <FormControl>
            <Input type="text" v-bind="componentField" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" :name="`${fieldName}[1]`">
        <FormItem class="space-y-0">
          <FormLabel class="sr-only">Close</FormLabel>
          <FormControl>
            <Input type="text" v-bind="componentField" />
          </FormControl>
          <FormMessage />
        </FormItem>
      </FormField>
    </template>
  </div>
</template>
