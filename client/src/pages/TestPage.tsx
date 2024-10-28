import { RootState } from "../store";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchPosts } from "../store/slices/postsSlice";
import { AppDispatch } from "../store";
import styles from "./TestPage.module.scss";

const TestPage: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { items, loading, error } = useSelector(
    (state: RootState) => state.products
  );

  useEffect(() => {
    dispatch(fetchPosts());
  }, [dispatch]);

  return (
    <div className={styles.container}>
      <h1>Test Page</h1>
      {items ? (
        items.length === 0 ? (
          <p className={styles.error}>Items {items.length} ❌</p>
        ) : (
          <p>Items: {items.length}✅</p>
        )
      ) : null}
      {loading && <p>Loading...</p>}
      {error && <p className={styles.error}>{error}</p>}
      <ul>
        {items.map((product) => (
          <li key={product.id} className={styles.success}>
            {product.title} - {product.body}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TestPage;
