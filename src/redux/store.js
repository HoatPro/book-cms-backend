import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import logger from 'redux-logger';
import createSagaMiddleware from 'redux-saga';
import reducers from './reducers';
import rootSaga from './saga';

const sagaMiddleware = createSagaMiddleware();
let middlewares = [sagaMiddleware];
if (process.env.NODE_ENV !== 'production') {
  middlewares = [...middlewares, logger];
}
const store = createStore(
  combineReducers({
    ...reducers,
  }),
  compose(applyMiddleware(...middlewares)),
);

sagaMiddleware.run(rootSaga);

export default store;
