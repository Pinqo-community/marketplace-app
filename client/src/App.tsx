import { Route, Routes } from "react-router-dom";
import OAuthRedirect from "./components/Auth/OAuthRedirect";
import HomePage from "./pages/HomePage/HomePage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/oauth/redirect" element={<OAuthRedirect />} />
    </Routes>
  );
}

export default App;
