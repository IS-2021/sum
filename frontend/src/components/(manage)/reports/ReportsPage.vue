<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { useGetReportsRestaurants } from '@/lib/api/reports-restaurants/reports-restaurants';
import { computed, unref } from 'vue';

import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const { data } = useGetReportsRestaurants({ restaurantId: props.restaurantId });
const reports = computed(() => unref(data)?.data);
</script>

<template>
  <div
    v-if="reports"
    class="max-w-screen-md flex-grow space-y-5 border border-neutral-200 bg-neutral-100 p-4"
  >
    <h2 class="font-semibold">Reports ({{ reports.length }})</h2>
    <p class="mt-4" v-if="reports.length === 0">No reports found</p>
    <ul v-else class="space-y-2">
      <li v-for="report in reports" v-bind:key="report.id">
        <ReportCard :report="report" />
      </li>
    </ul>
  </div>
</template>
