import {AppRegistry} from 'react-native';

import {name as appName} from './app.json';

// import App from './dapp/test/app';
import App from './src/main';
// console.log(App);
AppRegistry.registerComponent(appName, () => App);

