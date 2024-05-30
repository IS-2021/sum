<script setup lang="ts">
import { ref } from 'vue';
import type { Uuid } from '@/lib/api-model';

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
import { postReportsUsers } from '@/lib/api/reports-users/reports-users';
import DialogClose from '../ui/dialog/DialogClose.vue';

const props = defineProps<{
  restaurantId: Uuid;
  userId: Uuid;
  buttonMessage: string;
  bookingStatus: string;
}>();

const usersReport = ref('');
let reportRestaurantSwitch = ref(false);
let isReportSent = ref(false);

function toggleReportRestaurant() {
  reportRestaurantSwitch.value = !reportRestaurantSwitch.value;
}

function sendReport() {
  postReportsUsers({
    cause: usersReport.value,
    userId: props.userId,
    restaurantId: props.restaurantId,
  });
  usersReport.value = '';
  isReportSent.value = true;
}
</script>

<template>
  <Button
    :disabled="props.bookingStatus !== 'PickedUp'"
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
          <Alert v-if="isReportSent && usersReport === ''" class="mt-8 border-primary">
            <AlertTitle>Report sent successfully!</AlertTitle>
          </Alert>
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
