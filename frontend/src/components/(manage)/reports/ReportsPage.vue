<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { useGetReportsRestaurants } from '@/lib/api/reports-restaurants/reports-restaurants';
import { computed, unref } from 'vue';

import ReportCard from '../dashboard/ReportCard.vue';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const { data } = useGetReportsRestaurants({ restaurantId: props.restaurantId });
const reports = computed(() => unref(data)?.data);
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-10">Reports</h1>
  <div
    v-if="reports"
    class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 max-w-screen-md flex-shrink"
  >
    <h2 class="font-semibold">Ratings ({{ reports.length }})</h2>
    <p class="mt-4" v-if="reports.length === 0">No reports found</p>
    <ul v-else class="space-y-2">
      <li v-for="report in reports" v-bind:key="report.id">
        <ReportCard :cause="report.cause" :reportedAt="report.timestamp" />
      </li>
    </ul>
  </div>
</template>
