<script setup lang="ts">
import { ref } from 'vue';

import type { ProblemDetailResponse, Uuid } from '@/lib/api-model';

import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { Textarea } from '@/components/ui/textarea';
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import Button from '@/components/ui/button/Button.vue';
import DialogClose from '@/components/ui/dialog/DialogClose.vue';
import { toast } from 'vue-sonner';
import { postReportsRestaurants } from '@/lib/api/reports-restaurants/reports-restaurants';

const props = defineProps<{
  restaurantId: Uuid;
  userId: Uuid;
  buttonMessage: string;
  bookingStatus: string;
}>();

const usersReport = ref('');
let reportRestaurantSwitch = ref(false);

function toggleReportRestaurant() {
  reportRestaurantSwitch.value = !reportRestaurantSwitch.value;
}

async function sendReport() {
  const res = await postReportsRestaurants({
    cause: usersReport.value,
    userId: props.userId,
    restaurantId: props.restaurantId,
  });

  if (res.status === 200) {
    usersReport.value = '';
    toast.success('Report sent successfully!');
  } else if (res.status === 400) {
    const { detail } = res.data as unknown as ProblemDetailResponse;
    toast.error(detail);
  }
}
</script>

<template>
  <Button
    :disabled="props.bookingStatus === 'Active'"
    variant="ghost"
    @click="toggleReportRestaurant()"
    >{{ props.buttonMessage }}</Button
  >
  <Alert v-if="reportRestaurantSwitch" class="mt-4">
    <AlertTitle>Add report</AlertTitle>
    <AlertDescription>
      <Textarea placeholder="Type your message here." class="mt-4" v-model="usersReport" />
      <Dialog>
        <DialogTrigger as-child>
          <Button class="mt-4" :disabled="usersReport === ''">Send report</Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Are you sure tou want to send this report?</DialogTitle>
          </DialogHeader>
          <DialogFooter>
            <DialogClose>
              <Button type="submit" @click="sendReport"> Send report </Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </AlertDescription>
  </Alert>
</template>
