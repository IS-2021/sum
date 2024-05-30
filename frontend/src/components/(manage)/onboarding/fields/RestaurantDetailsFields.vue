<script setup lang="ts">
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { formFields } from '@/components/(manage)/onboarding/fields/fields';
</script>

<template>
  <FormField
    v-for="{ label, fieldName } in formFields.details"
    :key="fieldName"
    v-slot="{ componentField }"
    :name="fieldName"
  >
    <FormItem>
      <FormLabel>{{ label }}</FormLabel>
      <FormControl>
        <Input type="text" v-bind="componentField" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>
</template>
