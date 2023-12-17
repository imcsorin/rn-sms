import * as React from 'react';

import { StyleSheet, View, Pressable, Text } from 'react-native';
import { sendSMS } from 'rn-sms';

export default function App() {
  return (
    <View style={styles.container}>
      <Pressable
        onPress={async () => {
          const rsp = await sendSMS(
            ['3489345'],
            'This is a te$st SM%S to make sure %27 everything works co!l)rrectly:.'
          );
          console.log('response', rsp);
        }}
      >
        <Text style={styles.text}>Send sms</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  text: {
    color: 'white',
  },
});
