const LoadingOrFailed = ({loading}) => {
  return (
    loading
    ? <div>Loading...</div>
    : <div>Load Failed</div> // TODO add Retry button
  )
};

export default LoadingOrFailed;
