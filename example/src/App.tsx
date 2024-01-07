import * as React from 'react';

import { StyleSheet, Pressable, Text, Modal } from 'react-native';
import { sendSMS } from '@imcsorin/rn-sms';

export default function App() {
  return (
    <Modal style={styles.container} visible transparent>
      <Pressable
        onPress={async () => {
          const rsp = await sendSMS(
            ['+16288881096'],
            'This is a te$st SM%S to make sure %27 everything works co!l)rrectly:.'
          );
          console.log('response', rsp);
        }}
      >
        <Text style={styles.text}>Send sms</Text>
      </Pressable>
    </Modal>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'black',
    width: '100%',
    height: '100%',
  },
  text: {
    color: 'white',
    marginTop: 200,
    textAlign: 'center',
  },
});
