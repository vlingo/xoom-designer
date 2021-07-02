import React, {useCallback} from 'react';
import {createPortal} from 'react-dom';

const FormModal = ({title, show, close, children, submit}) => {

  const onSubmit = useCallback((e) => {
    e.preventDefault();
    submit(e);
    setTimeout(() => close(e), 200);
  }, [submit]);

  return createPortal(
    <>
      <form className={"modal fade" + (show ? ' show' : '')} style={{display: (show ? 'block': 'none')}}
        onSubmit={onSubmit}
        id="normalModal" tabIndex="-1"
        data-bs-backdrop="static" data-bs-keyboard="false"
        aria-labelledby="normalModalLabel" aria-hidden={!show} aria-modal={show} role={'dialog'}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="normalModalLabel">{title}</h5>
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" onClick={close}/>
              </div>
              <div className="modal-body">
                {children}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal" onClick={close}>Close</button>
                <button type="submit" className="btn btn-primary">Save changes</button>
              </div>
            </div>
          </div>
        </form>
      <div className="modal-backdrop fade-in show" id="backdrop" style={{display: (show ? 'block': 'none')}}/>
    </>,
    document.getElementById('modal-root')
  );
};

export default FormModal;
